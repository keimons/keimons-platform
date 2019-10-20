package com.keimons.platform.network.coder;

import com.keimons.platform.KeimonsTcpNet;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

@Sharable
public class ServerResponseEncoder<T> extends MessageToMessageEncoder<T> {

	public ServerResponseEncoder(Class<? extends T> outboundMessageType) {
		super(outboundMessageType);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void encode(ChannelHandlerContext ctx, T msg, List<Object> out) throws Exception {
		out.add(KeimonsTcpNet.ENCODE().coder(msg));
	}
}