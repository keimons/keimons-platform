package com.keimons.platform.network.coder;

import com.keimons.platform.network.IMessageConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@Sharable
public class ClientRequestDecoder<I> extends MessageToMessageDecoder<ByteBuf> {

	private final IMessageConverter<byte[], I> converter;

	public ClientRequestDecoder(IMessageConverter<byte[], I> converter) {
		this.converter = converter;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
		final byte[] array;
		if (msg.hasArray()) {
			array = msg.array();
		} else {
			int length = msg.readableBytes();
			array = new byte[length];
			msg.getBytes(msg.readerIndex(), array, 0, length);
		}
		out.add(converter.converter(ctx, array));
	}
}