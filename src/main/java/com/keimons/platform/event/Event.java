package com.keimons.platform.event;

import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.player.AbsPlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * 事件
 */
@Setter
@Getter
public class Event {

	/**
	 * 玩家Uuid
	 */
	private AbsPlayer player;

	/**
	 * 事件号
	 */
	private IEventCode eventCode;

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
	public <T> T getParams(int index) {
		return (T) params[index];
	}
}