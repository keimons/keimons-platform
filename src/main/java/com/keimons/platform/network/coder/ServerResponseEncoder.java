package com.keimons.platform.network.coder;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

//@Sharable
public class ServerResponseEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) {
		out.add(JSONObject.toJSONBytes(msg));
	}
}