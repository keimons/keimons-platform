package com.keimons.platform.network;

import com.keimons.basic.ICodecStrategy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty的半包粘包处理
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-25
 * @since 11
 **/
public class NettyTLVCodecPolicy implements ICodecStrategy<ByteBuf, byte[]> {


	@Override
	public byte[] decode(ByteBuf packet) throws Exception {
		return new byte[0];
	}

	@Override
	public ByteBuf encode(byte[] packet) throws Exception {
		ByteBuf buffer = Unpooled.buffer(1 + 4 + packet.length);
		buffer.writeByte(0);
		buffer.writeByte(packet.length);
		buffer.writeBytes(packet);
		return buffer;
	}
}