package com.keimons.platform.network;

import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.process.ProcessorManager;
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
public class KeimonsHandler extends SimpleChannelInboundHandler<Packet> {

	public static final AttributeKey<Session> SESSION = AttributeKey.valueOf("SESSION");

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Packet packet) {
		try {
			Session session = ctx.channel().attr(SESSION).get();
			if (session == null) {
				ctx.close();
				LogService.error("当前ctx无法获取Session，Session已经被销毁");
				return;
			}
			IProcessor processor = ProcessorManager.getProcessor(packet.getMsgCode());
			if (processor != null) {
				long start = TimeUtil.currentTimeMillis();
				AProcessor aProcessor = ProcessorManager.getMsgCodeInfo(packet.getMsgCode());
				processor.processor(session, packet);
				long end = TimeUtil.currentTimeMillis();
				if (end - start > 100) {
					LogService.info(TimeUtil.getDateTime() + " 超长消息执行：" + aProcessor.MsgCode() + "，执行时长：" + (end - start));
				}
			} else {
				LogService.error("不存在的消息号：" + packet.getMsgCode());
			}
		} catch (Exception e) {
			String info = "错误号：" + packet.getMsgCode() + "，会话ID：" + ctx.channel().attr(SESSION).get();
			LogService.error(e, info);
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
		// 已经是pipeline中最后一个handler了
		Session session = ctx.channel().attr(SESSION).get();
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
		Session session = ctx.channel().attr(SESSION).get();
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
	 * @param ctx 通道
	 * @throws Exception 错误
	 */
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		super.channelWritabilityChanged(ctx);
	}
}