package com.keimons.platform.process;

import com.keimons.platform.KeimonsConfig;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.ICoder;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 消息处理管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class ProcessorManager {

	/**
	 * 消息处理器
	 */
	private static Map<Integer, ProcessorInfo<?>> processors = new HashMap<>();

	/**
	 * 获取消息号
	 */
	private static ICoder<?, Integer> msg_code;

	public static <T> void setMsgCode(ICoder<T, Integer> msg_code) {
		ProcessorManager.msg_code = msg_code;
	}

	/**
	 * 选择适当的执行器
	 *
	 * @param session 会话
	 * @param packet  消息体
	 * @param <T>     参数类型
	 * @throws Exception 转码错误
	 */
	@SuppressWarnings("unchecked")
	public static <T> void execute(Session session, T packet) throws Exception {
		int msgCode = ((ICoder<T, Integer>) msg_code).coder(packet);
		ProcessorInfo<T> processorInfo = (ProcessorInfo<T>) processors.get(msgCode);
		KeimonsExecutor<T> instance = KeimonsExecutor.getInstance();
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
	 *
	 * @param packageName 消息处理器
	 */
	public static void addProcessor(String packageName) {
		List<Class<IProcessor<?>>> classes = ClassUtil.loadClasses(packageName, AProcessor.class);
		for (Class<IProcessor<?>> clazz : classes) {
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
				IProcessor<?> processor = clazz.getDeclaredConstructor().newInstance();
				processors.put(info.MsgCode(), new ProcessorInfo<>(info, processor));
				System.out.println("消息处理器：" + "消息号：" + info.MsgCode() + "，描述：" + info.Desc());
			} catch (Exception e) {
				LogService.error(e, clazz.getSimpleName() + "安装消息处理器失败");
			}
			System.out.println("成功安装消息处理器：" + clazz.getSimpleName());
		}
	}
}