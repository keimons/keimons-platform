package com.keimons.platform.network.coder;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@Sharable
public class ServerResponseEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) {
		if (msg instanceof MessageLite) {
			out.add(((MessageLite) msg).toByteArray());
		} else if (msg instanceof MessageLite.Builder) {
			out.add(((MessageLite.Builder) msg).build().toByteArray());
		} else {
			out.add(msg);
		}
	}
}