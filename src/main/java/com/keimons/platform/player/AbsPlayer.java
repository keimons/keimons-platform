package com.keimons.platform.player;

import com.google.protobuf.MessageLite;
import com.keimons.platform.iface.IOtherDay;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.TimeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Setter
@Getter
public abstract class AbsPlayer {

	/**
	 * 玩家锁，玩家上线下线时用
	 */
	private Lock lock = new ReentrantLock();

	/**
	 * 会话
	 */
	private Session session;

	/**
	 * 储存玩家所有数据
	 */
	private HashMap<Class<? extends IPlayerData>, IPlayerData> modules = new HashMap<>();

	/**
	 * 最后活跃时间
	 */
	private volatile long lastActiveTime;

	@SuppressWarnings("unchecked")
	public <T extends IPlayerData> T getModule(Class<T> clazz) {
		return (T) modules.get(clazz);
	}

	/**
	 * 增加玩家数据
	 *
	 * @param data
	 */
	public void addPlayerData(IPlayerData data) {
		modules.put(data.getClass(), data);
	}

	/**
	 * 获取玩家所有的模块数据
	 *
	 * @return
	 */
	public Collection<IPlayerData> getModules() {
		return modules.values();
	}

	public boolean hasModule(Class<? extends IPlayerData> clazz) {
		return modules.containsKey(clazz);
	}

	public void otherDay0(long flushTime) {
		for (IPlayerData data : modules.values()) {
			if (data instanceof IOtherDay) {
				((IOtherDay) data).otherDay0(this, !TimeUtil.isSameWeek(flushTime, TimeUtil.currentTimeMillis()), !TimeUtil.isSameMonth(flushTime, TimeUtil.currentTimeMillis()));
			}
		}
	}

	/**
	 * 跨天5点
	 *
	 * @param flushTime 上次刷新时间
	 * @param now       当前时间
	 */
	public void otherDay5(long flushTime, long now) {
		flushTime -= 5 * 60 * 60 * 1000;
		now -= 5 * 60 * 60 * 1000;
		boolean otherWeek = !TimeUtil.isSameWeek(flushTime, now);
		boolean otherMonth = !TimeUtil.isSameMonth(flushTime, now);
		for (IPlayerData data : modules.values()) {
			if (data instanceof IOtherDay) {
				((IOtherDay) data).otherDay5(this, otherWeek, otherMonth);
			}
		}
	}

	/**
	 * 发送消息至客户端
	 *
	 * @param msgCode  消息号
	 * @param errCodes 错误号
	 */
	public void send(int msgCode, String... errCodes) {
		send(msgCode, (byte[]) null, errCodes);
	}

	/**
	 * 发送消息至客户端
	 *
	 * @param msgCode  消息号
	 * @param msg      消息体
	 * @param errCodes 错误号
	 */
	public void send(int msgCode, MessageLite msg, String... errCodes) {
		send(msgCode, msg == null ? null : msg.toByteArray(), errCodes);
	}

	/**
	 * 发送消息至客户端
	 *
	 * @param msgCode  消息号
	 * @param data     数据体
	 * @param errCodes 错误号
	 */
	public void send(int msgCode, byte[] data, String... errCodes) {
		if (session != null) {
			session.send(msgCode, data, errCodes);
		}
	}

	abstract void kick();
}