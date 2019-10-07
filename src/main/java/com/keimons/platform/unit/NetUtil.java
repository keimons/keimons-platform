package com.keimons.platform.unit;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * 网络相关工具
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class NetUtil {

	/**
	 * 获取连接的IP地址
	 *
	 * @param ctx 连接
	 * @return IP地址
	 */
	public static String getIpAddress(ChannelHandlerContext ctx) {
		if (ctx != null) {
			InetSocketAddress socket = (InetSocketAddress) ctx.channel().remoteAddress();
			return socket.getAddress().getHostAddress();
		} else {
			return "0.0.0.0";
		}
	}
}