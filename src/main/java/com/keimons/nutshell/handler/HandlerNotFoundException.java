package com.keimons.nutshell.handler;

/**
 * 处理器未找到异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class HandlerNotFoundException extends RuntimeException {

	/**
	 * 消息处理器查找失败异常
	 *
	 * @param msgCode 消息号
	 */
	public HandlerNotFoundException(int msgCode) {
		super("msg code " + msgCode + " handler not found");
	}
}