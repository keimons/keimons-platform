package com.keimons.platform.session;

import com.keimons.platform.event.EventManager;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 会话，每一个玩家都依赖于一个会话 <br />
 * Session的销毁是一个非常复杂的过程 <br />
 * 当玩家通道被关闭时，立即关闭会话，玩家每次登陆，都会创建一个新的ctx，也就会创建一个新的会话
 */
public class Session {

	/**
	 * session唯一ID
	 */
	private static AtomicInteger atomicInteger = new AtomicInteger(0);

	private ChannelHandlerContext ctx;

	/**
	 * 最后活跃时间
	 */
	private long lastActiveTime;

	/**
	 * 会话是否存活
	 */
	private volatile boolean connect;

	/**
	 * 是否登录
	 */
	private volatile boolean isLogin;

	/**
	 * session
	 */
	private int sessionId;

	/**
	 * 玩家
	 */
	private AbsPlayer player;

	public int getSessionId() {
		return sessionId;
	}

	public boolean isConnect() {
		return connect;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public Session setLogin(boolean login) {
		isLogin = login;
		return this;
	}

	public AbsPlayer getPlayer() {
		return player;
	}

	public void setPlayer(AbsPlayer player) {
		this.player = player;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public Session(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.lastActiveTime = System.currentTimeMillis();
		this.connect = true;
		this.sessionId = atomicInteger.getAndIncrement();
	}

	/**
	 * 断开连接<br />
	 * 此处玩家只是标识玩家连接断开，但是实际上Session和Player并没有被真正的从内存中移除<br />
	 */
	public void disconnect() {
		String time = TimeUtil.logDate();
		if (player != null) {
			player.setSession(null);
			EventManager.publicEvent(player, "PLAYER_DOWNLINE");
			player = null;
		}
		if (ctx != null) {
			ctx.close();
			ctx = null;
		}
		connect = false;
		isLogin = false;
	}

	/**
	 * 发送消息到客户端
	 *
	 * @param msgCode      消息号
	 * @param data         数据
	 * @param errCode      错误号
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Session) {
			Session session = (Session) obj;
			return sessionId == session.getSessionId();
		}
		return false;
	}
}