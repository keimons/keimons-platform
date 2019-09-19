package com.keimons.platform.session;

import com.keimons.platform.network.Packet;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * 会话，每一个玩家都依赖于一个会话 <br />
 * Session的销毁是一个非常复杂的过程 <br />
 * 当玩家通道被关闭时，立即关闭会话，玩家每次登陆，都会创建一个新的ctx，也就会创建一个新的会话
 */
@Setter
@Getter
public class Session {

	/**
	 * 客户端服务器连接
	 */
	private ChannelHandlerContext ctx;

	/**
	 * 最后活跃时间
	 */
	private long lastActiveTime = TimeUtil.currentTimeMillis();

	/**
	 * 会话是否存活
	 */
	private volatile boolean connect = true;

	/**
	 * 是否登录
	 */
	private volatile boolean isLogin;

	/**
	 * 玩家
	 */
	private AbsPlayer player;

	public Session(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 断开连接<br />
	 * 此处玩家只是标识玩家连接断开，但是实际上Session和Player并没有被真正的从内存中移除<br />
	 */
	public void disconnect() {
		if (player != null) {
			player.setSession(null);
		}
		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
		player = null;
		connect = false;
		isLogin = false;
	}

	/**
	 * 发送消息到客户端
	 *
	 * @param msgCode 消息号
	 * @param data    数据
	 * @param errCode 错误号
	 */
	public void send(int msgCode, byte[] data, String... errCode) {
		Packet packet = new Packet();
		packet.setMsgCode(msgCode);
		if (data != null) {
			packet.setData(data);
		}
		packet.setErrCodes(errCode);
		send(packet);
	}

	/**
	 * 发送消息
	 *
	 * @param msg 消息
	 * @param <T> 实现
	 */
	public <T extends Packet> void send(T msg) {
		if (connect) {
			if (msg != null) {
				ctx.writeAndFlush(msg);
			}
		}
	}

	/**
	 * 获取连接的IP地址
	 *
	 * @return 连接IP
	 */
	public String getIpAddress() {
		if (ctx != null) {
			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
			return insocket.getAddress().getHostAddress();
		} else {
			return "0.0.0.0";
		}
	}
}