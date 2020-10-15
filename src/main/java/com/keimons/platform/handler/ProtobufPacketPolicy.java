package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import com.keimons.platform.process.IPacketParseStrategy;

/**
 * Protobuf包体解析策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufPacketPolicy implements IPacketParseStrategy<PbPacket.Packet, ByteString> {

	@Override
	public PbPacket.Packet parsePacket(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
	}

	@Override
	public int findMsgCode(PbPacket.Packet packet) {
		return packet.getMsgCode();
	}

	@Override
	public ByteString findData(PbPacket.Packet packet) {
		return packet.getData();
	}
}