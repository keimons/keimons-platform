package com.keimons.platform.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerResponseFrameEncoder extends MessageToByteEncoder<byte[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) {
		out.ensureWritable(1 + 4 + msg.length);
		out.writeByte(0);
		out.writeInt(msg.length);
		out.writeBytes(msg);
	}
}