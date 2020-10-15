package com.keimons.platform.process;

import com.keimons.platform.session.ISession;

/**
 * 线程选择策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IThreadStrategy<SessionT extends ISession, MessageT> {

	/**
	 * 线程选择
	 *
	 * @param session 客户端-服务器会话
	 * @param message 消息体
	 * @return 线程选择会话
	 */
	default int threadCode(SessionT session, MessageT message) {
		return 0;
	}
}