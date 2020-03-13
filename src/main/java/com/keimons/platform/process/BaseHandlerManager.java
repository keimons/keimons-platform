package com.keimons.platform.process;

import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.executor.IExecutor;
import com.keimons.platform.executor.IExecutorEnum;
import com.keimons.platform.session.ISession;
import com.keimons.platform.unit.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 消息处理管理器
 * <p>
 * 消息处理器封装了3个消息执行器，系统共计定义了3个线程模型。分别命名为短耗时线程，长耗时线程，
 * 可自定义分配线程。系统消息默认由短耗时线程处理，短耗时线程中，所有的消息都是执行时间短，执行速度
 * 快，不涉及或消耗极小的IO/串行的操作。长耗时线程中是执行速度慢或执行时间不确定的线程。可自定义分
 * 配线程是用来处理有特殊需要的功能，例如，公会操作等。另外，类似于一键申请的功能，建议放在一个单独
 * 的单线程中执行，可以避免并发问题。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseHandlerManager<SessionT extends ISession, MessageT> implements IHandlerManager<SessionT, MessageT> {

	/**
	 * 线程执行器
	 */
	private final IExecutor executor;

	/**
	 * 消息处理器
	 */
	private final Map<Integer, IHandler<SessionT, MessageT>> handlers = new HashMap<>();

	public BaseHandlerManager(IExecutor executor) {
		this.executor = executor;
	}

	/**
	 * 消息处理
	 *
	 * @param session 客户端服务器会话
	 * @param message 消息体
	 */
	@Override
	public void handler(SessionT session, MessageT message) {
		int msgCode = this.getMsgCode(message);
		IHandler<SessionT, MessageT> info = handlers.get(msgCode);

		Enum<? extends IExecutorEnum> config = info.getExecutorType();
		SelectionThreadFunction<SessionT, MessageT> rule = info.getRule();
		if (rule == null) {
			executor.execute(config, info.createTask(session, message));
		} else {
			int maxIndex = ((IExecutorEnum) config).getThreadNumb();
			int index = rule.selection(session, message, maxIndex);
			executor.execute(config, index, info.createTask(session, message));
		}
	}

	/**
	 * 从消息体中获取消息号
	 *
	 * @param message 消息体
	 * @return 消息号
	 */
	protected abstract int getMsgCode(MessageT message);

	/**
	 * 添加消息号
	 * <p>
	 * 扫描指定位置下的所有Class文件，如果该类标注了{@link AProcessor}根据消息号进行
	 * 消息号的添加，整个功能都是按照消息号来求情的，消息号所有操作的标识。当消息号被加载
	 * 到系统时，需要校验消息号是否能处理底层系统并对消息号进行校验。
	 *
	 * @param packageName 消息号所在包
	 * @param annotation  扫描的注解
	 * @param creator     转化函数
	 */
	protected void addJavaProcessor(
			String packageName,
			Class<? extends Annotation> annotation,
			Function<? super Class<IProcessor<SessionT, MessageT>>, ? extends IHandler<SessionT, MessageT>> creator
	) {
		List<Class<IProcessor<SessionT, MessageT>>> classes = ClassUtil.loadClasses(packageName, annotation);
		for (Class<IProcessor<SessionT, MessageT>> clazz : classes) {
			IHandler<SessionT, MessageT> handler = creator.apply(clazz);
			addProcessor(handler);
		}
	}

	/**
	 * 增加一个消息号
	 *
	 * @param handler 消息信息
	 */
	public void addProcessor(IHandler<SessionT, MessageT> handler) {
		if (handlers.containsKey(handler.getMsgCode())) {
			throw new ModuleException("重复的消息号：" + handler.getMsgCode());
		}
		handlers.put(handler.getMsgCode(), handler);
		System.out.println("消息处理器：" + "消息号：" + handler.getMsgCode()
				+ "，描述：" + handler.getDesc());
	}
}