package com.keimons.nutshell.network.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * 默认数据传输模式
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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
			byte target = in.readByte();
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
			ByteBuffer byteBuffer = in.nioBuffer(in.readerIndex(), length);
			if (in.readableBytes() <= 0) {
				in.clear();
			}
			out.add(array);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
}