package com.keimons.platform.network;

import com.keimons.basic.ICodecStrategy;
import jdk.internal.vm.annotation.ForceInline;

/**
 * Protobuf包体解析策略
 * <p>
 * 示例程序：将消息解析成{@link PbPacket.Packet}对象。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufCodecPolicy implements ICodecStrategy<byte[], PbPacket.Packet> {

	@ForceInline
	public PbPacket.Packet decode(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
	}

	@Override
	public byte[] encode(PbPacket.Packet packet) throws Exception {
		return packet.toByteArray();
	}
}