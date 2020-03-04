package com.keimons.platform.network;

import com.keimons.platform.log.LogService;
import com.keimons.platform.session.ISession;
import com.keimons.platform.session.Session;
import com.keimons.platform.session.SessionManager;
import com.keimons.platform.unit.NetUtil;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * 消息最终处理器
 * <p>
 * 入栈事件在pipeline中流动的最后一个节点，出栈事件在pipeline中流动的第一个节点
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class KeimonsHandler<I> extends SimpleChannelInboundHandler<I> {

	public static final AttributeKey<ISession> SESSION = AttributeKey.valueOf("SESSION");

	public KeimonsHandler(Class<I> messageType) {
		super(messageType);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, I packet) {
		try {
			ISession session = ctx.channel().attr(SESSION).get();
			if (session == null) {
				ctx.close();
				LogService.error("当前ctx无法获取Session，Session已经被销毁");
				return;
			}
			session.commit(packet);
		} catch (Exception e) {
			String info = "会话ID：" + ctx.channel().attr(SESSION).get();
			LogService.error(e, info);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		ISession session = new Session(ctx);
		ctx.channel().attr(KeimonsHandler.SESSION).set(session);
		SessionManager.getInstance().addSession(session);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		ISession session = ctx.channel().attr(SESSION).get();
		if (session != null) {
			session.disconnect();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 已经是pipeline中最后一个handler了
		ISession session = ctx.channel().attr(SESSION).get();
		if (session != null) {
			String errInfo = "Netty Exception! SessionId: " + NetUtil.getIpAddress(ctx);
			LogService.error(errInfo);
			session.disconnect();
		} else {
			ctx.close();
		}
		LogService.error(cause, "未能在应用中捕获的异常");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		ISession session = ctx.channel().attr(SESSION).get();
		if (session != null) {
			session.disconnect();
		}
	}

	/**
	 * 事件被触发
	 *
	 * @param ctx 连接
	 * @param evt 事件
	 * @throws Exception 异常
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		// 读写超时时调用
		if (evt instanceof IdleStateEvent) {
			// 如果5分钟没有发生读/写的操作，则认定客户端已经掉线，直接关闭会话
			ISession session = ctx.channel().attr(SESSION).get();
			if (session != null && TimeUtil.currentTimeMillis() - session.getLastActiveTime() >= 5 * 60 * 1000) {
				session.disconnect();
			}
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		System.out.println("开启连接：" + address.getHostString());
		super.channelActive(ctx);
	}

	/**
	 * 水位线变化
	 *
	 * @param ctx 通道
	 * @throws Exception 错误
	 */
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		super.channelWritabilityChanged(ctx);
	}
}