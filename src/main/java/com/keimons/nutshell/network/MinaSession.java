package com.keimons.nutshell.network;

import org.apache.mina.core.session.IoSession;

/**
 * 客户端-服务器连接
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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