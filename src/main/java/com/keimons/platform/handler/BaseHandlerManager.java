package com.keimons.platform.handler;

import com.keimons.platform.Keimons;
import com.keimons.platform.Optional;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.executor.CommitterManager;
import com.keimons.platform.process.AProcessor;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.ISession;
import com.keimons.platform.unit.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * 消息处理管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseHandlerManager<SessionT extends ISession, PacketT, DataT>
		implements IHandlerManager<SessionT, byte[]> {

	/**
	 * 消息解析策略
	 */
	protected final IPacketParseStrategy<PacketT, DataT> packetStrategy = Keimons.get(Optional.MESSAGE_PARSE);

	/**
	 * 消息处理器
	 */
	protected final Map<Integer, IHandler<SessionT, DataT, ?>> handlers = new HashMap<>();

	@Override
	public void handler(SessionT session, byte[] bytes) {
		handler0(session, bytes);
	}

	/**
	 * 消息处理
	 *
	 * @param session    客户端-服务器会话
	 * @param bytes      原始消息体
	 * @param <MessageT> 消息体类型
	 */
	private <MessageT> void handler0(SessionT session, byte[] bytes) {
		PacketT packet;
		try {
			packet = packetStrategy.parsePacket(bytes);
		} catch (Exception e) {
			exceptionCaught(session, bytes, new PacketParseException(e));
			return;
		}
		int msgCode = packetStrategy.findMsgCode(packet);
		IHandler<SessionT, DataT, MessageT> handler = findHandler(msgCode);
		if (handler == null) {
			exceptionCaught(session, bytes, new HandlerNotFoundException(msgCode));
			return;
		}
		DataT data = packetStrategy.findData(packet);
		MessageT message;
		try {
			message = handler.parseMessage(data);
		} catch (Exception e) {
			exceptionCaught(session, bytes, e);
			return;
		}

		IProcessor<SessionT, MessageT> processor = handler.getProcessor();

		// 调用任务提交策略提交任务
		CommitterManager.commitTask(
				handler.getCommitterStrategy(),
				session.getExecutorCode(),
				handler.getExecutorStrategy(),
				processor.threadCode(session, message),
				() -> processor.processor(session, message)
		);
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
	protected <T extends Annotation, MessageT> void addHandler(
			String packageName,
			Class<T> annotation,
			BiFunction<? super Class<IProcessor<SessionT, MessageT>>, T,
								? extends IHandler<SessionT, DataT, MessageT>> creator
	) {
		List<Class<IProcessor<SessionT, MessageT>>> classes = ClassUtil.findClasses(packageName, annotation);
		for (Class<IProcessor<SessionT, MessageT>> clazz : classes) {
			IHandler<SessionT, DataT, ?> handler = creator.apply(clazz, clazz.getAnnotation(annotation));
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
	 * <p>
	 * 可能出现的异常：
	 * <ul>
	 *     <li>{@link PacketParseException}消息解析异常</li>
	 *     <li>{@link HandlerNotFoundException}消息处理器未找到异常</li>
	 * </ul>
	 *
	 * @param session 客户端-服务器会话
	 * @param raw     原始数据
	 * @param cause   异常信息
	 */
	public abstract void exceptionCaught(SessionT session, byte[] raw, Throwable cause);
}