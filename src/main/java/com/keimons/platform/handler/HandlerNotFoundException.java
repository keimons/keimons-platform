package com.keimons.platform.handler;

/**
 * 处理器未找到异常
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-09-14
 * @since 1.8
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