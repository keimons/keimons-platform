package com.keimons.platform.session;

import com.keimons.platform.iface.IManager;
import com.keimons.platform.log.LogService;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionManager implements IManager {

	/**
	 * 所有会话
	 */
	private static ConcurrentLinkedDeque<Session> sessions = new ConcurrentLinkedDeque<>();

	/**
	 * 服务器是否运行中
	 */
	private volatile boolean running = true;

	/**
	 * 增加一个客户端-服务器会话
	 *
	 * @param session 会话
	 */
	public static void addSession(Session session) {
		sessions.add(session);
	}

	/**
	 * 会话存活检测
	 */
	private void update() {
		while (running) {
			try {
				// 缓存当前时间是一件非常危险的事，因为在迭代时，时间依然在走 考虑到 nowTime - Session#getLastActiveTime < 0 并没有问题
				long nowTime = System.currentTimeMillis();
				Iterator<Session> iterator = sessions.iterator();
				while (iterator.hasNext()) {
					Session session = iterator.next();
					// 连接已经断开 或 5分钟没有心跳
					if (!session.isConnect() || nowTime - session.getLastActiveTime() > 5 * 60 * 1000) {
						session.disconnect();
						iterator.remove();
					}
				}
				Thread.sleep(1000L);
			} catch (Exception e) {
				LogService.error(e);
			}
		}
	}

	@Override
	public void init() {
		// TODO 优化为系统自带定时任务
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(this::update);
	}

	@Override
	public void reload() {
		System.out.println(this.getClass().toString() + "!!!");
	}

	@Override
	public boolean shutdown() {
		running = false;
		for (Session session : sessions) {
			session.disconnect();
		}
		return true;
	}
}