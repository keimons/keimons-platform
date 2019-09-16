package com.keimons.platform.network.process;

import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.session.Session;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 根消息处理器
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

	public final Parser<? extends Message> getParser() {
		return parser;
	}

	@SuppressWarnings("unchecked")
	public BaseProcessor() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			Class<T> clazz = (Class<T>) superclass.getActualTypeArguments()[0];
			T instance = Internal.getDefaultInstance(clazz);
			parser = instance.getParserForType();
		}
	}

	@SuppressWarnings("unchecked")
	public void processor(Session session, Packet packet) {
		Packet msg = null;
		try {
			// 注册或者登录等没有Player的情况
			if (parser == null) {
				if (session.isLogin()) {
					msg = process(session.getPlayer());
				} else {
					msg = process(session);
				}
			} else {
				T request;
				try {
					request = (T) parser.parseFrom(packet.getData());
				} catch (Exception e) {
					LogService.error(e);
					// 通知客户端服务器错误
					Packet pt = new Packet();
					pt.setMsgCode(packet.getMsgCode() + 1).setErrCodes(new String[]{"EncodeError"});
					session.send(pt);
					return;
				}
				if (session.isLogin()) {
					msg = process(session.getPlayer(), request);
				} else {
					msg = process(session, request);
				}
			}
		} catch (Exception e) {
			LogService.error(e);
			// 通知客户端服务器错误
			Packet pt = new Packet();
			pt.setErrCodes(new String[]{"SystemError"});
			pt.setMsgCode(packet.getMsgCode() + 1);
			session.send(pt);
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
}