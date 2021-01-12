package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import jdk.internal.vm.annotation.ForceInline;

/**
 * Protobuf包体解析策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufPacketPolicy implements IPacketStrategy<PbPacket.Packet, ByteString> {

	@ForceInline
	@Override
	public int findMsgCode(PbPacket.Packet packet) {
		return packet.getMsgCode();
	}

	@ForceInline
	@Override
	public ByteString findData(PbPacket.Packet packet) {
		return packet.getData();
	}
}