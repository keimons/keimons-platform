package com.keimons.platform.process;

import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.session.Session;
import com.keimons.platform.thread.DefaultExecutorConfig;
import com.keimons.platform.thread.KeimonsExecutor;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class HandlerManager {

	private static KeimonsExecutor executor = new KeimonsExecutor(DefaultExecutorConfig.class);

	/**
	 * 消息处理器
	 */
	public static Map<Integer, IHandler<?>> processors = new HashMap<>();

	/**
	 * 映射函数
	 */
	public static Function<Object, Integer> mapping;

	/**
	 * 入站出站消息类型
	 * <p>
	 * 用户自定义消息的入站出站类型，消息在pipeline中流动时，经历byte->对象的转化
	 * 这里提供的事入站出站消息使用的类型，校验每一个消息号使用的入站出站数据类型是否
	 * 和框架中使用的相同。
	 */
	private final Class<?> messageType;

	/**
	 * 构造器
	 *
	 * @param messageType 入站出站消息类型
	 */
	public HandlerManager(Class<?> messageType, Function<Object, Integer> mapping) {
		this.messageType = messageType;
		HandlerManager.mapping = mapping;
	}

	public static <I> boolean handler(Session session, I packet) throws Exception {
		IHandler<I> info = (IHandler<I>) processors.get(mapping.apply(packet));
		return info.handler(session, packet);
	}

	/**
	 * 添加消息号
	 * <p>
	 * 扫描指定位置下的所有Class文件，如果该类标注了{@link AProcessor}根据消息号进行
	 * 消息号的添加，整个功能都是按照消息号来求情的，消息号所有操作的标识。当消息号被加载
	 * 到系统时，需要校验消息号是否能处理底层系统并对消息号进行校验。
	 *
	 * @param packageName 消息号所在包
	 */
	public <I> void addProcessor(String packageName) {
		List<Class<BaseProcessor<?>>> classes = ClassUtil.loadClasses(packageName, AProcessor.class);
		for (Class<BaseProcessor<?>> clazz : classes) {
			AProcessor info = clazz.getAnnotation(AProcessor.class);
			if (processors.containsKey(info.MsgCode()) &&
					!clazz.getName().equals(processors.get(info.MsgCode()).getClass().getName())) {
				throw new ModuleException("重复的消息号：" + clazz.getName() + "，与：" + processors.get(info.MsgCode()).getClass().getName());
			}
			BaseProcessor<?> instance = null;
			try {
				instance = clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				LogService.error(e, clazz.getSimpleName() + "，安装消息处理器失败");
				System.exit(1);
			}

			Class<?> type = ClassUtil.find(instance, BaseProcessor.class, "T");

			if (!type.equals(messageType)) {
				throw new ModuleException("载体信息错误" + clazz.getName() + "，与：" + processors.get(info.MsgCode()).getClass().getName());
			}

			@SuppressWarnings("unchecked")
			BaseProcessor<I> processor = (BaseProcessor<I>) instance;

			processors.put(info.MsgCode(), processor);
			System.out.println("消息处理器：" + "消息号：" + info.MsgCode() + "，描述：" + info.Desc());
			System.out.println("成功安装消息处理器：" + clazz.getSimpleName());
		}
	}
}