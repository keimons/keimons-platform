package com.keimons.platform.player;

import com.google.protobuf.MessageLite;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.module.Modules;
import com.keimons.platform.session.Session;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 玩家数据
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class Player implements IPlayer {

	/**
	 * 玩家锁，玩家上线下线时用
	 */
	private Lock lock = new ReentrantLock();

	/**
	 * 会话
	 */
	protected Session session;

	/**
	 * 数据唯一标识符号
	 */
	private final String identifier;

	/**
	 * 玩家身上所有的模块组件
	 */
	protected Modules modules;

	/**
	 * 构造方法
	 *
	 * @param identifier 数据唯一标识符
	 */
	public Player(String identifier) {
		this.identifier = identifier;
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

	public Lock getLock() {
		return lock;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void setModules(Modules modules) {
		this.modules = modules;
	}

	@Override
	public <T extends IPlayerData> T getModule(String moduleName) {
		return modules.getModule(moduleName);
	}

	@Override
	public Collection<IPlayerData> getModules() {
		return modules.getModules();
	}
}