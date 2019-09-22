package com.keimons.platform.network.coder;

import com.keimons.platform.network.KeimonsHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by monkey on 2019/09/12.
 */
public class KeimonsServiceInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) {
		ch.pipeline()
				.addLast("IdleHandler", new IdleStateHandler(5 * 60, 5 * 60, 5 * 60))
				.addLast("FramerDecoder", new ClientRequestFrameDecoder())
				.addLast("RequestDecoder", new ClientRequestDecoder())
				.addLast("FramerEncoder", new ServerResponseFrameEncoder())
				.addLast("RequestEncoder", new ServerResponseEncoder())
				.addLast("KeimonsHandler", new KeimonsHandler());
	}
}
