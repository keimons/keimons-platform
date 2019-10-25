package com.keimons.platform.process;

import com.keimons.platform.KeimonsConfig;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.MessageConverter;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * 消息处理管理器
 * <p>
 * 消息处理器封装了3层消息执行器，系统共计定义了3个线程模型。分别命名为短耗时线程，中耗时线程，
 * 长耗时线程。系统消息默认由短耗时线程处理，短耗时线程中，所有的消息都是执行时间短，执行速度
 * 快，不涉及或消耗极小的IO/串行的操作。中耗时线程中，可以由用户自行将消息路由给中耗时线程来执
 * 行，例如，根据联盟ID将该联盟的所有操作，路由到某一个线程来执行，可以避免联盟内部的很多同步。
 * 长耗时线程用于处理设计IO/串行的操作，因为在IO/串行过程中，CPU并不会保持长时间的占用状态，
 * 所以，可以多设置一些线程。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class ProcessorManager<I> {

	/**
	 * 消息处理器
	 */
	private Map<Integer, ProcessorInfo<I>> processors = new HashMap<>();

	/**
	 * 通过消息获取消息号
	 */
	private final Function<I, Integer> message2Code;

	/**
	 * 入站出站消息类型
	 * <p>
	 * 用户自定义消息的入站出站类型，消息在pipeline中流动时，经历byte->对象的转化
	 * 这里提供的事入站出站消息使用的类型，校验每一个消息号使用的入站出站数据类型是否
	 * 和框架中使用的相同。
	 */
	private final Class<I> messageType;

	/**
	 * 构造器
	 *
	 * @param message2Code 消息号提取器
	 * @param messageType  入站出站消息类型
	 */
	public ProcessorManager(Function<I, Integer> message2Code, Class<I> messageType) {
		this.message2Code = message2Code;
		this.messageType = messageType;
	}

	/**
	 * 选择适当的执行器
	 *
	 * @param session 会话
	 * @param packet  消息体
	 */
	public void execute(Session session, I packet) {
		int msgCode = message2Code.apply(packet);
		ProcessorInfo<I> processorInfo = processors.get(msgCode);
		KeimonsExecutor<I> instance = KeimonsExecutor.getInstance();
		if (processorInfo != null) {
			switch (processorInfo.selectThreadLevel()) {
				case H_LEVEL:
					instance.asyncTopProcessor(session, processorInfo, packet);
					break;
				case M_LEVEL:
					instance.asyncMidProcessor(session, processorInfo, packet);
					break;
				case L_LEVEL:
					instance.asyncLowProcessor(session, processorInfo, packet);
					break;
				default:
			}
		} else {
			LogService.error("不存在的消息号：" + msgCode);
		}
	}

	/**
	 * 选择执行器并执行消息体
	 *
	 * @param threadName 线程名
	 * @param callable   执行内容
	 * @param <T>        返回值类型
	 * @return 执行结果
	 */
	public static <T> T execute(String threadName, Callable<T> callable) {
		try {
			return KeimonsExecutor.syncProcessor(threadName, callable);
		} catch (ExecutionException | InterruptedException e) {
			LogService.error(e);
		}
		return null;
	}

	/**
	 * 选择执行器并执行消息体
	 *
	 * @param threadName 线程名
	 * @param runnable   执行内容
	 */
	public static void execute(String threadName, Runnable runnable) {
		KeimonsExecutor.syncProcessor(threadName, runnable);
	}

	/**
	 * 添加消息号
	 * <p>
	 * 扫描指定位置下的所有Class文件，如果该类标注了{@link AProcessor}根据消息号进行
	 * 消息号的添加，整个功能都是按照消息号来求情的，消息号所有操作的标识。当消息号被加载
	 * 到系统时，需要校验消息号是否能处理底层系统
	 * {@link MessageConverter#getInboundConverter()}并对消息号进行校验。
	 *
	 * @param packageName 消息号所在包
	 */
	public void addProcessor(String packageName) {
		List<Class<BaseProcessor<?>>> classes = ClassUtil.loadClasses(packageName, AProcessor.class);
		for (Class<BaseProcessor<?>> clazz : classes) {
			AProcessor info = clazz.getAnnotation(AProcessor.class);
			if (info.ThreadLevel() == ThreadLevel.AUTO &&
					!KeimonsServer.KeimonsConfig.isAutoThreadLevel()) {
				throw new ModuleException("消息处理器不允许配置自适应线程池，因为未启用配置项：" + KeimonsConfig.DEFAULT_NET_THREAD_AUTO);
			}
			if ((info.ThreadLevel() == ThreadLevel.M_LEVEL &&
					KeimonsServer.KeimonsConfig.getNetThreadCount()[1] <= 0) ||
					KeimonsServer.KeimonsConfig.getNetThreadLevel()[1] < 0) {
				throw new ModuleException("消息处理器不允许配置中执行速度线程池，因为中执行速度线程池未开启");
			}
			if (info.ThreadLevel() == ThreadLevel.L_LEVEL &&
					KeimonsServer.KeimonsConfig.getNetThreadCount()[2] <= 0) {
				throw new ModuleException("消息处理器不允许配置低执行速度线程池，因为低执行速度线程池未开启");
			}
			if (processors.containsKey(info.MsgCode()) &&
					!clazz.getName().equals(processors.get(info.MsgCode()).getClass().getName())) {
				throw new ModuleException("重复的消息号：" + clazz.getName() + "，与：" + processors.get(info.MsgCode()).getClass().getName());
			}
			try {
				@SuppressWarnings("unchecked")
				BaseProcessor<I> processor = (BaseProcessor<I>) clazz.getDeclaredConstructor().newInstance();

				if (!processor.getMessageType().equals(messageType)) {
					throw new ModuleException("数据载体格式错误：" + getClass().getName());
				}

				processors.put(info.MsgCode(), new ProcessorInfo<>(info, processor));
				System.out.println("消息处理器：" + "消息号：" + info.MsgCode() + "，描述：" + info.Desc());
			} catch (Exception e) {
				LogService.error(e, clazz.getSimpleName() + "，安装消息处理器失败");
			}
			System.out.println("成功安装消息处理器：" + clazz.getSimpleName());
		}
	}
}