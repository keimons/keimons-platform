package com.keimons.platform.network;

import org.apache.mina.core.session.IoSession;

/**
 * 客户端-服务器连接
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-29
 * @since 1.8
 **/
public class MinaSession implements ISession {

	/**
	 * 原始连接
	 */
	private IoSession session;

	/**
	 * 构造器
	 *
	 * @param session 原始连接
	 */
	public MinaSession(IoSession session) {
		this.session = session;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IoSession getSession() {
		return session;
	}

	@Override
	public MinaWriteFuture write(Object object) {
		return new MinaWriteFuture(this, session.write(object));
	}
}