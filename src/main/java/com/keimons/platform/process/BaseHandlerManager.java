package com.keimons.platform.process;

import com.keimons.platform.Keimons;
import com.keimons.platform.Optional;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.executor.IExecutor;
import com.keimons.platform.executor.IExecutorStrategy;
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
public abstract class BaseHandlerManager<SessionT extends ISession, PacketT, DataT>
		implements IHandlerManager<SessionT> {

	/**
	 * 线程执行器
	 */
	private final IExecutor executor;

	/**
	 * 消息解析策略
	 */
	private final IPacketParseStrategy<PacketT, DataT> packetStrategy;

	/**
	 * 消息处理器
	 */
	private final Map<Integer, IHandler<SessionT, DataT, ?>> handlers;

	/**
	 * 构造方法
	 *
	 * @param executor 业务执行器
	 */
	public BaseHandlerManager(IExecutor executor) {
		this.executor = executor;
		this.handlers = new HashMap<>();
		this.packetStrategy = Keimons.get(Optional.MESSAGE_PARSE);
	}

	/**
	 * 消息处理
	 *
	 * @param session 客户端服务器会话
	 * @param raw     原始消息
	 */
	@Override
	public void handler(SessionT session, byte[] raw) {
		handler0(session, raw);
	}

	public <MessageT> void handler0(SessionT session, byte[] raw) {
		PacketT packet;
		try {
			packet = packetStrategy.parsePacket(raw);
		} catch (Exception e) {
			exceptionCaught(session, raw, new PacketParseException(e));
			return;
		}
		int msgCode = packetStrategy.findMsgCode(packet);
		IHandler<SessionT, DataT, MessageT> handler = findHandler(msgCode);
		if (handler == null) {
			exceptionCaught(session, raw, new HandlerNotFoundException(msgCode));
			return;
		}
		DataT data = packetStrategy.findData(packet);
		MessageT message;
		try {
			message = handler.parseMessage(data);
		} catch (Exception e) {
			exceptionCaught(session, raw, e);
			return;
		}

		IProcessor<SessionT, MessageT> processor = handler.getProcessor();

		IExecutorStrategy<SessionT, MessageT> executorStrategy = handler.getExecutorStrategy();
		if (executorStrategy == null) {
			session.commit0(() -> processor.processor(session, message));
		} else {
			executorStrategy.execute(session, message);
		}

		int threadCode = processor.threadCode(session, message);
//		Enum<? extends IExecutorType> config = handler.getExecutorType();
//		SelectionThreadFunction<SessionT, MessageT> rule = handler.getRule();
//
//		Runnable task = handler.createTask(session, message);
//		if (rule == null) {
//			executor.execute(config, task);
//		} else {
//			int maxIndex = ((IExecutorType) config).getThreadNumb();
//			int index = rule.selection(session, message, maxIndex);
//			executor.execute(config, index, task);
//		}
	}

	/**
	 * 查找消息号
	 *
	 * @param msgCode 消息号
	 * @return 消息处理器
	 */
	@SuppressWarnings("unchecked")
	public <MessageT> IHandler<SessionT, DataT, MessageT> findHandler(int msgCode) {
		return (IHandler<SessionT, DataT, MessageT>) handlers.get(msgCode);
	}

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
	protected void addHandler(
			String packageName,
			Class<? extends Annotation> annotation,
			Function<? super Class<IProcessor<SessionT, ?>>,
					? extends IHandler<SessionT, DataT, ?>> creator
	) {
		List<Class<IProcessor<SessionT, ?>>> classes = ClassUtil.findClasses(packageName, annotation);
		for (Class<IProcessor<SessionT, ?>> clazz : classes) {
			IHandler<SessionT, DataT, ?> handler = creator.apply(clazz);
			addHandler(handler);
		}
	}

	/**
	 * 增加一个消息号
	 *
	 * @param handler 消息信息
	 */
	public void addHandler(IHandler<SessionT, DataT, ?> handler) {
		if (handlers.containsKey(handler.getMsgCode())) {
			throw new ModuleException("重复的消息号：" + handler.getMsgCode());
		}
		handlers.put(handler.getMsgCode(), handler);
		System.out.println("消息处理器：" + "消息号：" + handler.getMsgCode()
				+ "，描述：" + handler.getDesc());
	}

	/**
	 * 获取消息解析策略
	 *
	 * @return 消息解析策略
	 */
	public IPacketParseStrategy<PacketT, DataT> getPacketStrategy() {
		return packetStrategy;
	}

	/**
	 * 异常处理
	 *
	 * @param session 客户端-服务器会话
	 * @param raw     原始数据
	 * @param cause   异常信息
	 */
	public abstract void exceptionCaught(SessionT session, byte[] raw, Throwable cause);
}