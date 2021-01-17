package com.keimons.platform.handler;

import jdk.internal.vm.annotation.ForceInline;

/**
 * Protobuf包体解析策略
 * <p>
 * 示例程序：将消息解析成{@link com.keimons.platform.handler.PbPacket.Packet}对象。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufParserPolicy implements IParserStrategy<PbPacket.Packet> {

	@ForceInline
	@Override
	public PbPacket.Packet parsePacket(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
	}
}