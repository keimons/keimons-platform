package com.keimons.platform.handler;

import com.keimons.platform.session.ISession;

/**
 * 消息处理接接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IHandlerManager<SessionT extends ISession, InboundT> {

	/**
	 * 消息处理
	 *
	 * @param session 客户端-服务器会话
	 * @param message 消息体
	 */
	void handler(SessionT session, InboundT message);
}