package com.keimons.platform.handler;

import com.keimons.platform.session.ISession;

/**
 * 消息处理接接口
 *
 * @param <PacketT>  包体类型
 * @param <SessionT> 会话类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IHandlerStrategy<SessionT extends ISession, PacketT> {

	/**
	 * 消息处理
	 *
	 * @param session 客户端-服务器会话
	 * @param packet  消息体
	 */
	void handler(SessionT session, PacketT packet);
}