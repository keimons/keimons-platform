package com.keimons.platform.network.process;

import com.google.protobuf.*;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.session.Session;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 消息处理器
 */
public abstract class BaseProcessor<T extends Message> implements IProcessor {

	/**
	 * 消息解析器
	 * <p>
	 * 整个设计中最核心的部分之一
	 * <p>
	 * 通过参数化类型，查找到该对象的class文件
	 * 然后通过反射获取到该消息的消息解析器
	 */
	private Parser<? extends Message> parser;

	public BaseProcessor() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) superclass.getActualTypeArguments()[0];
			T instance = Internal.getDefaultInstance(clazz);
			parser = instance.getParserForType();
		}
	}

	public void processor(Session session, Packet packet) {
		AbsPlayer player = session.getPlayer();
		if (session.isLogined() && player == null) {
			session.disconnect();
			LogService.error("极限情况，连接已经关闭但是仍有未处理完的消息");
			return;
		}
		Packet msg;
		try {
			if (parser == null) {
				if (session.isLogined()) {
					msg = process(player);
				} else {
					msg = process(session);
				}
			} else {
				@SuppressWarnings("unchecked")
				T request = (T) parser.parseFrom(packet.getData());
				if (session.isLogined()) {
					msg = process(player, request);
				} else {
					msg = process(session, request);
				}
			}
		} catch (Exception e) {
			String[] errCodes = new String[1];
			if (e instanceof InvalidProtocolBufferException) {
				LogService.error(e, "数据解析失败");
				errCodes[0] = "DecodeError";
			} else {
				LogService.error(e, "消息执行失败");
				errCodes[0] = "ExecutorError";
			}
			// 通知客户端服务器错误
			Packet pt = new Packet();
			pt.setErrCodes(errCodes);
			pt.setMsgCode(packet.getMsgCode() + 1);
			session.send(pt);
			return;
		}
		if (msg != null) {
			session.send(msg.setMsgCode(packet.getMsgCode() + 1));
		}
	}

	/**
	 * 玩家不存在时调用
	 *
	 * @param session 会话
	 */
	public Packet process(Session session) {
		return null;
	}

	/**
	 * 玩家不存在时调用
	 *
	 * @param session 会话
	 * @param request 消息体
	 */
	public Packet process(Session session, T request) {
		return null;
	}

	/**
	 * 玩家存在时调用
	 *
	 * @param player 玩家
	 */
	public Packet process(AbsPlayer player) {
		return null;
	}

	/**
	 * 玩家存在时调用
	 *
	 * @param player 玩家
	 */
	public Packet process(AbsPlayer player, T request) {
		return null;
	}

	/**
	 * 构造返回消息
	 *
	 * @param errCodes 错误号
	 * @return 消息体
	 */
	public Packet build(String... errCodes) {
		return build(null, errCodes);
	}

	/**
	 * 构造返回消息
	 *
	 * @param data     下行消息
	 * @param errCodes 错误号
	 * @return 消息体
	 */
	public Packet build(MessageLite data, String... errCodes) {
		Packet packet = new Packet();
		packet.setErrCodes(errCodes);
		if (data != null) {
			packet.setData(data.toByteArray());
		}
		return packet;
	}

	public final Parser<? extends Message> getParser() {
		return parser;
	}
}