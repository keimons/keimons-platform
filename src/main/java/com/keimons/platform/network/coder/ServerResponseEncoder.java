package com.keimons.platform.network.coder;

import com.keimons.platform.network.IMessageConverter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@Sharable
public class ServerResponseEncoder<I> extends MessageToMessageEncoder<I> {

	private final IMessageConverter<I, byte[]> converter;

	public ServerResponseEncoder(Class<I> messageType, IMessageConverter<I, byte[]> converter) {
		super(messageType);
		this.converter = converter;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, I msg, List<Object> out) {
		out.add(converter.converter(ctx, msg));
	}
}