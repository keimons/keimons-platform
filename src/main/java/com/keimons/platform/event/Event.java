package com.keimons.platform.event;

import com.keimons.platform.player.IPlayer;

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
	private IPlayer<?> player;

	/**
	 * 事件号
	 */
	private Enum<? extends IEventCode<?>> eventCode;

	/**
	 * 参数列表
	 */
	private Object[] params;

	/**
	 * 获取参数
	 *
	 * @param index 下标
	 * @param <T>   类型
	 * @return 参数
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParam(int index) {
		return (T) params[index];
	}

	@SuppressWarnings("unchecked")
	public <T extends Enum<T> & IEventCode<?>> T getEventCode() {
		return (T) eventCode;
	}

	@SuppressWarnings("unchecked")
	public <T extends IPlayer<?>> T getPlayer() {
		return (T) player;
	}

	public void setPlayer(IPlayer<?> player) {
		this.player = player;
	}

	public void setEventCode(Enum<? extends IEventCode<?>> eventCode) {
		this.eventCode = eventCode;
	}

	public void setParams(Object... params) {
		this.params = params;
	}
}