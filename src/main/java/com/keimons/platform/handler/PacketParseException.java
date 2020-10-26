package com.keimons.platform.handler;

/**
 * 消息体解析异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class PacketParseException extends RuntimeException {

	/**
	 * 异常信息
	 *
	 * @param e 异常
	 */
	public PacketParseException(Exception e) {
		super("packet parse exception", e);
	}
}