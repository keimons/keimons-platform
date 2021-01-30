package com.keimons.platform.network;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-29
 * @since 1.8
 **/
public class NettySession implements ISession<ChannelHandlerContext> {

	@Override
	public IFuture write(Object object) {
		return null;
	}

	@Override
	public IFuture write(Object object, Object object2) {
		return null;
	}
}