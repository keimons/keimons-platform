package com.keimons.platform.process;

import com.google.protobuf.*;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.BasePlayer;
import com.keimons.platform.session.Session;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 消息处理器
 *
 * @param <T> 解码器 如果没有解码器则代码没有数据
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseProcessor<T extends Message> implements IProcessor {

	/**
	 * 客户端数据解码器
	 * <p>
	 * 通过参数化类型，查找到该对象的class文件
	 * 然后通过反射获取到该消息的解码器
	 */
	private final Parser<? extends Message> parser;

	/**
	 * 消息号
	 * <p>
	 * 消息号总是成对的，每一个下行消息都会对应一个上行消息
	 */
	protected final int msgCode;

	public BaseProcessor() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) superclass.getActualTypeArguments()[0];
			T instance = Internal.getDefaultInstance(clazz);
			parser = instance.getParserForType();
		} else {
			parser = null;
		}
		Class<? extends BaseProcessor> clazz = this.getClass();
		if (clazz.isAnnotationPresent(AProcessor.class)) {
			AProcessor processorInfo = clazz.getAnnotation(AProcessor.class);
			this.msgCode = processorInfo.MsgCode();
		} else {
			throw new NullPointerException("查找@AProcessor注解失败");
		}
	}

	@Override
	public void processor(Session session, Packet packet) {
		BasePlayer player = session.getPlayer();
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
			pt.setMsgCode(msgCode + 1);
			session.send(pt);
			return;
		}
		if (msg != null) {
			session.send(msg.setMsgCode(msgCode + 1));
		}
	}

	/**
	 * 玩家不存在时调用
	 *
	 * @param session 会话
	 * @return 返回消息
	 */
	public Packet process(Session session) {
		return null;
	}

	/**
	 * 玩家不存在时调用
	 *
	 * @param session 会话
	 * @param request 消息体
	 * @return 返回消息
	 */
	public Packet process(Session session, T request) {
		return null;
	}

	/**
	 * 玩家存在时调用
	 *
	 * @param player 玩家
	 * @return 返回消息
	 */
	public Packet process(BasePlayer player) {
		return null;
	}

	/**
	 * 玩家存在时调用
	 *
	 * @param player  玩家
	 * @param request 请求
	 * @return 返回消息
	 */
	public Packet process(BasePlayer player, T request) {
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
	 * @return 返回消息
	 */
	public Packet build(MessageLite data, String... errCodes) {
		Packet packet = new Packet();
		packet.setErrCodes(errCodes);
		packet.setMsgCode(msgCode + 1);
		if (data != null) {
			packet.setData(data.toByteArray());
		}
		return packet;
	}

	public final int getMsgCode() {
		return msgCode;
	}

	public final Parser<? extends Message> getParser() {
		return parser;
	}
}