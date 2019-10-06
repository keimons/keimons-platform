package com.keimons.platform.event;

import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.player.BasePlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * 事件
 * 减少对象的创建，采用环形Buffer实现
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.8
 */
@Setter
@Getter
public class Event {

	/**
	 * 玩家Uuid
	 */
	private BasePlayer player;

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
}