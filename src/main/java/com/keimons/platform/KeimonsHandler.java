package com.keimons.platform;

import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.network.process.IProcessor;
import com.keimons.platform.network.process.ProcessorManager;
import com.keimons.platform.session.Session;
import com.keimons.platform.session.SessionManager;
import com.keimons.platform.unit.NetUtil;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class KeimonsHandler extends ChannelInboundHandlerAdapter {

	public static final AttributeKey<Session> SESSION = AttributeKey.valueOf("SESSION");

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Packet packet = null;
		try {
			if (msg instanceof Packet) {
				packet = (Packet) msg;
				Session session = ctx.channel().attr(SESSION).get();
				if (session == null) {
					ctx.close();
					LogService.error("极限情况，无法在ctx中获取Session，ctx已经被销毁");
					return;
				}
				// 直接发送到世界服
				IProcessor processor = ProcessorManager.getProcessor(packet.getMsgCode());
				if (processor != null) {
					long start = TimeUtil.currentTimeMillis();
					AProcessor aProcessor = ProcessorManager.getMsgCodeInfo(packet.getMsgCode());
					processor.processor(session, packet);
					long end = TimeUtil.currentTimeMillis();
					if (end - start > 100) {
						LogService.info(TimeUtil.logDate() + " 超长消息执行：" + aProcessor.MsgCode() + "，执行时长：" + (end - start));
					}
				} else {
					LogService.error("不存在的消息号：" + packet.getMsgCode());
				}
			}
		} catch (Exception e) {
			String info = "错误号：" + packet.getMsgCode() + "，会话ID：" + ctx.channel().attr(SESSION).get();
			LogService.error(e, info);
		} finally {
			// 释放消息体
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		Session session = new Session(ctx);
		ctx.channel().attr(KeimonsHandler.SESSION).set(session);
		SessionManager.getInstance().addSession(session);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		Session session = ctx.channel().attr(SESSION).get();
		if (session != null) {
			session.disconnect();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Session session = ctx.channel().attr(SESSION).get();
		if (session != null) {
			String errInfo = "Netty Exception! SessionId: " + NetUtil.getIpAddress(ctx);
			LogService.error(errInfo);
			session.disconnect();
		}
		LogService.error(cause, "未能在应用中捕获的异常");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
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
			Session session = ctx.channel().attr(SESSION).get();
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
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		super.channelWritabilityChanged(ctx);
	}
}