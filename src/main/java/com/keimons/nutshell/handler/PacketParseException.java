package com.keimons.nutshell.handler;

/**
 * 消息体解析异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class PacketParseException extends RuntimeException {

	/**
	 * 异常信息
	 *
	 * @param e 异常
	 */
	public PacketParseException(Throwable e) {
		super("packet parse exception", e);
	}
}