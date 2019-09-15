package com.keimons.platform.network.coder;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@Sharable
public class ClientRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		final byte[] array;
		if (msg.hasArray()) {
			array = msg.array();
		} else {
			int length = msg.readableBytes();
			array = new byte[length];
			msg.getBytes(msg.readerIndex(), array, 0, length);
		}
		out.add(JSONObject.parseObject(array, Packet.class));
	}
}