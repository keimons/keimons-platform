package com.keimons.nutshell.event;

/**
 * 事件处理器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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