package com.keimons.nutshell.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 客户端-服务器连接
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class NettySession implements ISession {

	/**
	 * 原始连接
	 */
	private final ChannelHandlerContext ctx;

	/**
	 * 构造器
	 *
	 * @param ctx 原始连接
	 */
	public NettySession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		AttributeKey<ISession> key = AttributeKey.valueOf("1234");
		ISession session = this.ctx.channel().attr(key).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ChannelHandlerContext getSession() {
		return ctx;
	}

	@Override
	public NettyWriteFuture write(Object object) {
		return new NettyWriteFuture(this, ctx.writeAndFlush(object));
	}
}