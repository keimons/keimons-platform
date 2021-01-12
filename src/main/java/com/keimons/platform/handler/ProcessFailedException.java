package com.keimons.platform.handler;

/**
 * 消息处理失败异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProcessFailedException extends RuntimeException {

	/**
	 * 消息处理失败异常
	 *
	 * @param msgCode 消息号
	 * @param message 消息体
	 * @param e       错误信息
	 */
	public ProcessFailedException(int msgCode, String message, Throwable e) {
		super("msg code " + msgCode + " process failed, info " + message, e);
	}
}