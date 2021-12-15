package com.keimons.nutshell.session;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;

/**
 * 会话管理器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class SessionManager {

	// region 单例模式
	/**
	 * 管理器实例
	 */
	private static SessionManager instance;

	/**
	 * 单例模式，Session管理器
	 */
	private SessionManager() {

	}

	/**
	 * 会话管理实例
	 *
	 * @return 会话管理
	 */
	public static SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (instance == null) {
					instance = new SessionManager();
				}
			}
		}
		return instance;
	}
	// endregion

	/**
	 * 缓存整个游戏中所有的缓存
	 */
	private Set<ISession> sessions = new ConcurrentSet<>();

	/**
	 * 增加一个客户端-服务器会话
	 *
	 * @param session 会话
	 */
	public void addSession(ISession session) {
		sessions.add(session);
	}

	/**
	 * 移除客户端-服务器会话
	 *
	 * @param session 会话
	 */
	public void removeSession(ISession session) {
		sessions.remove(session);
	}

	/**
	 * 关闭服务器
	 */
	public void shutdown() {
		for (ISession session : sessions) {
			session.disconnect();
		}
	}
}