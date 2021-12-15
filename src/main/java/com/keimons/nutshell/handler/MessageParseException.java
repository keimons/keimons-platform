package com.keimons.nutshell.handler;

/**
 * 消息解析异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class MessageParseException extends RuntimeException {

	/**
	 * 消息解析异常
	 */
	public MessageParseException(Throwable e) {
		super("message parse error", e);
	}
}