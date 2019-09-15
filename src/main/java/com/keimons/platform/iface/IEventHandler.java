package com.keimons.platform.iface;

import com.keimons.platform.event.Event;

/**
 * 事件处理接口
 */
public interface IEventHandler {

	/**
	 * 注册
	 *
	 * @return 关注的消息号
	 */
	IEventCode[] register();

	/**
	 * 事件处理
	 * @param event 事件
	 */
	void handler(Event event);
}