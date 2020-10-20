package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import com.keimons.platform.process.BaseHandlerManager;
import com.keimons.platform.session.Session;

/**
 * 默认实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufHandlerManager extends BaseHandlerManager<Session, PbPacket.Packet, ByteString> {

	@Override
	public void exceptionCaught(Session session, byte[] raw, Throwable cause) {
		cause.printStackTrace();
	}
}