package com.keimons.platform.session;

import com.keimons.platform.iface.IManager;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.TimeUtil;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager implements IManager {

	/**
	 * 所有会话
	 */
	private static ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();

	/**
	 * 服务器是否
	 */
	private volatile boolean running = true;

	public static Session getSession(int id) {
		return sessions.get(id);
	}

	public static void addSession(Session session) {
		sessions.put(session.getSessionId(), session);
	}

	/**
	 * 会话检测
	 *
	 * @return
	 */
	public void update() {
		while (running) {
			try {
				long nowTime = System.currentTimeMillis();
				Iterator<Session> iterator = sessions.values().iterator();
				while (iterator.hasNext()) {
					Session session = iterator.next();
					// 玩家断开连接或者5分钟没有活动
					if (!session.isConnect() || nowTime - session.getLastActiveTime() > 10 * 60 * 1000) {
						session.disconnect();
						// 会话会在移除后十分钟后彻底销毁
						if (nowTime - session.getLastActiveTime() > 10 * 60 * 1000) {
							sessions.remove(session.getSessionId());
							LogService.getInstance().info(TimeUtil.logDate() + " 彻底移除会话：" + session.getSessionId());
						}
					}
				}
				Thread.sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init() {
		Thread update = new Thread(this::update, "THREAD-EXECUTE");
		update.start();
	}

	@Override
	public void reload() {
		System.out.println(this.getClass().toString() + "!!!");
	}

	@Override
	public boolean shutdown() {
		running = false;
		for (Session session : sessions.values()) {
			session.disconnect();
		}
		return true;
	}
}