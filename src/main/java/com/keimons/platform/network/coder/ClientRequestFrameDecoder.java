package com.keimons.platform.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ClientRequestFrameDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		while (true) {
			in.markReaderIndex();
			if (in.readableBytes() < 5) {
				return;
			}
			// 消息长度
			byte type = in.readByte();
			int length = in.readInt();
			if (length > in.readableBytes()) {
				// 半包，重新设置读的位置 还原到上次读取的位置
				in.resetReaderIndex();
				return;
			}

			// 消息体
			ByteBuf bytes = in.readBytes(length);
			if (in.readableBytes() <= 0) {
				in.clear();
			}
			out.add(bytes);
		}
	}
}