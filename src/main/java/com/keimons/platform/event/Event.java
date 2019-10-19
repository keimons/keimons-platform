package com.keimons.platform.event;

import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.player.Player;

/**
 * 事件
 * 减少对象的创建，采用环形Buffer实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class Event {

	/**
	 * 玩家Uuid
	 */
	private Player player;

	/**
	 * 事件号
	 */
	private Enum<? extends IEventCode> eventCode;

	/**
	 * 参数列表
	 */
	private Object[] params;

	@SuppressWarnings("unchecked")
	public <T extends Enum<T> & IEventCode> T getEventCode() {
		return (T) eventCode;
	}

	/**
	 * 获取参数
	 *
	 * @param index 下标
	 * @param <T>   类型
	 * @return 参数
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParams(int index) {
		return (T) params[index];
	}

	public Player getPlayer() {
		return player;
	}

	public Event setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public Event setEventCode(Enum<? extends IEventCode> eventCode) {
		this.eventCode = eventCode;
		return this;
	}

	public Event setParams(Object[] params) {
		this.params = params;
		return this;
	}
}