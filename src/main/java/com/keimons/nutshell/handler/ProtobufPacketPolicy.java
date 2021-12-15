package com.keimons.nutshell.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.keimons.nutshell.basic.IPacketStrategy;
import com.keimons.nutshell.network.PbPacket;

/**
 * Protobuf包体解析策略
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProtobufPacketPolicy implements IPacketStrategy<PbPacket.Packet, ByteString> {

	@Override
	public int findMsgCode(PbPacket.Packet packet) {
		return packet.getMsgCode();
	}

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