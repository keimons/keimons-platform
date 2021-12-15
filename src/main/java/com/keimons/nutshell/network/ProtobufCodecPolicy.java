package com.keimons.nutshell.network;

import com.keimons.nutshell.basic.ICodecStrategy;

/**
 * Protobuf包体解析策略
 * <p>
 * 示例程序：将消息解析成{@link PbPacket.Packet}对象。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProtobufCodecPolicy implements ICodecStrategy<byte[], PbPacket.Packet> {

	@Override
	public PbPacket.Packet decode(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
	}

	@Override
	public byte[] encode(PbPacket.Packet packet) throws Exception {
		return packet.toByteArray();
	}
}