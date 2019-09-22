package com.keimons.platform.session;

import com.keimons.platform.log.LogService;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 会话管理器
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-20
 * @since 1.8
 */
public class SessionManager {

	// region 单例模式
	/**
	 * 管理器实例
	 */
	private static SessionManager instance;

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
	private ConcurrentLinkedDeque<Session> sessions = new ConcurrentLinkedDeque<>();

	/**
	 * 增加一个客户端-服务器会话
	 *
	 * @param session 会话
	 */
	public void addSession(Session session) {
		sessions.add(session);
	}

	/**
	 * 会话存活检测 每5秒执行一次
	 */
	public void update() {
		// 缓存当前时间是一件非常危险的事，因为在迭代时，时间依然在走 考虑到 nowTime - Session#getLastActiveTime < 0 并没有问题
		long nowTime = System.currentTimeMillis();
		Iterator<Session> iterator = sessions.iterator();
		while (iterator.hasNext()) {
			try {
				Session session = iterator.next();
				// 连接已经断开 或 5分钟没有心跳
				if (!session.isConnect() || nowTime - session.getLastActiveTime() > 5 * 60 * 1000) {
					session.disconnect();
					iterator.remove();
				}
			} catch (Exception e) {
				LogService.error(e);
			}
		}
	}

	public void shutdown() {
		for (Session session : sessions) {
			session.disconnect();
		}
	}
}