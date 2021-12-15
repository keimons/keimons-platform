package com.keimons.nutshell.handler;

import com.keimons.nutshell.session.ISession;

/**
 * 消息处理接接口
 *
 * @param <PacketT>  包体类型
 * @param <SessionT> 会话类型
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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