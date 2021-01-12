package com.keimons.platform.handler;

/**
 * 消息解析异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class MessageParseException extends RuntimeException {

	/**
	 * 消息解析异常
	 */
	public MessageParseException(Throwable e) {
		super("message parse error", e);
	}
}