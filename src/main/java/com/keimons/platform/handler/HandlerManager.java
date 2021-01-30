package com.keimons.platform.handler;

import com.keimons.platform.StrategyAlreadyExistsException;
import com.keimons.platform.session.ISession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 消息处理管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class HandlerManager {

	@SuppressWarnings("unchecked")
	private static final IHandlerStrategy<ISession, Object>[] strategies = new IHandlerStrategy[127];

	public static final int JSON_HANDLER = 0;

	public static final int PROTOBUF_HANDLER = 1;

	/**
	 * 默认消息处理器
	 */
	private static int DEFAULT_HANDLER = 0;

	static {
		registerHandlerStrategy(JSON_HANDLER, new JsonHandlerPolicy(0));
		registerHandlerStrategy(PROTOBUF_HANDLER, new ProtobufHandlerPolicy(1));
	}

	/**
	 * 获取一个消息处理策略
	 *
	 * @param executorIndex 消息处理器
	 * @return 消息处理策略
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IHandlerStrategy<?, ?>> T getHandlerStrategy(
			@Range(from = 0, to = 127) int executorIndex) {
		return (T) strategies[executorIndex];
	}

	/**
	 * 使用默认消息处理策略，处理消息
	 *
	 * @param session 客户端-服务器会话
	 * @param packet  包体
	 */
	public static <SessionT extends ISession, Packet> void defaultHandler(
			SessionT session, Packet packet) {
		strategies[DEFAULT_HANDLER].handler(session, packet);
	}

	/**
	 * 使用消息处理策略，处理消息
	 *
	 * @param <PacketT>    包体类型
	 * @param <SessionT>   会话类型
	 * @param handlerIndex 消息处理策略
	 * @param session      客户端-服务器会话
	 * @param packet       包体
	 */
	public static <SessionT extends ISession, PacketT> void handler(
			int handlerIndex, SessionT session, PacketT packet) {
		strategies[handlerIndex].handler(session, packet);
	}

	/**
	 * 注册一个消息处理策略
	 *
	 * @param <PacketT>    包体类型
	 * @param <SessionT>   会话类型
	 * @param handlerIndex 消息处理器器
	 * @param strategy     消息处理策略
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <SessionT extends ISession, PacketT> void registerHandlerStrategy(
			@Range(from = 0, to = 127) int handlerIndex,
			@NotNull IHandlerStrategy<SessionT, PacketT> strategy) {
		if (Objects.nonNull(strategies[handlerIndex])) {
			throw new StrategyAlreadyExistsException("handler", handlerIndex);
		}
		strategies[handlerIndex] = (IHandlerStrategy<ISession, Object>) strategy;
	}

	public static int getDefaultHandler() {
		return DEFAULT_HANDLER;
	}

	public static void setDefaultHandler(int defaultHandler) {
		DEFAULT_HANDLER = defaultHandler;
	}
}