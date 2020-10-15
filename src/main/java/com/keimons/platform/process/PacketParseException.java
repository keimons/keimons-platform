package com.keimons.platform.process;

/**
 * 消息体解析异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class PacketParseException extends RuntimeException {

	public PacketParseException(Exception e) {
		super("error packet", e);
	}
}