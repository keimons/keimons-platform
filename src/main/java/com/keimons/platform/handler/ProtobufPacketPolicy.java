package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.keimons.basic.IPacketStrategy;
import com.keimons.platform.network.PbPacket;
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

	/**
	 * 构造一个消息体
	 *
	 * @param msgCode 消息号
	 * @param errCode 错误号
	 * @param message 消息体
	 * @return 消息体
	 */
	public PbPacket.Packet createPacket(int msgCode, String errCode, MessageLiteOrBuilder message) {
		PbPacket.Packet.Builder builder = PbPacket.Packet.newBuilder();
		builder.setMsgCode(msgCode);
		if (errCode != null) {
			builder.setErrCode(errCode);
		}
		if (message != null) {
			if (message instanceof MessageLite) {
				builder.setData(((MessageLite) message).toByteString());
			} else {
				builder.setData(((MessageLite.Builder) message).build().toByteString());
			}
		}
		return builder.build();
	}
}