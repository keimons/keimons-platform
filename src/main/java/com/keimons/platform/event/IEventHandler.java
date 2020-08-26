package com.keimons.platform.event;

/**
 * 事件处理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public interface IEventHandler {

	/**
	 * 注册事件
	 *
	 * @return 关注的消息号
	 */
	IEventCode[] register();

	/**
	 * 事件处理
	 *
	 * @param event 事件
	 */
	void handler(Event event);
}