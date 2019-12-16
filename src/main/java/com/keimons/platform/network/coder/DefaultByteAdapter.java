package com.keimons.platform.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2019-12-04
 * @since 1.8
 **/
public class DefaultByteAdapter extends ByteAdapter {

	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
		out.ensureWritable(1 + 4 + msg.length);
		out.writeByte(0);
		out.writeInt(msg.length);
		out.writeBytes(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		for (; ; ) {
			in.markReaderIndex();
			if (in.readableBytes() < 5) {
				return;
			}
			// 消息类型
			byte type = in.readByte();
			// 消息长度
			int length = in.readInt();
			if (length > in.readableBytes()) {
				// 半包，重新设置读的位置 还原到上次读取的位置
				in.resetReaderIndex();
				return;
			}
			// 读取出来消息体
			final byte[] array = new byte[length];
			in.getBytes(in.readerIndex(), array, 0, length);
			// 设置读取的位置
			in.readerIndex(in.readerIndex() + length);
			if (in.readableBytes() <= 0) {
				in.clear();
			}
			out.add(array);
		}
	}
}