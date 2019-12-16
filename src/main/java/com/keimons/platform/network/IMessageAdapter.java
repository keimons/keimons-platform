package com.keimons.platform.network;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息转化接口
 *
 * @param <T> 消息
 * @param <R> 转化结果
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IMessageAdapter<T, R> {

	/**
	 * 消息转化
	 *
	 * @param data 入栈消息
	 * @param ctx  客户端-服务器连接
	 * @return 转换后的消息
	 */
	R converter(ChannelHandlerContext ctx, T data);
}