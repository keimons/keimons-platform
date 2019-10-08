package com.keimons.platform.session;

import com.keimons.platform.log.LogService;
import com.keimons.platform.network.KeimonsHandler;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.BasePlayer;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 会话，每一个ctx都会附带一个会话，每一个玩家也会依赖于一个会话
 * <p>
 * Session的销毁依托于ctx的关闭，当ctx长时间空闲或直接关闭时Session也会销毁
 * <p>
 * 当客户端-服务器连接关闭时，会话也会被立即关闭，玩家每次登陆，都会创建一个新的ctx，也就会创建一个新的会话
 * <p>
 * 注意：ctx关闭时，有可能有还未处理完的消息，如果消息已经开始执行了，那么该消息会被执行完。
 * ctx中的Session已经被移除了，所以无法通过ctx找到Session进而找到玩家，如果消息依然在排队，那么消息将被直接丢弃
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class Session {

	/**
	 * 客户端-服务器连接
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
	 * <p>
	 * 该值只有在未登录时为{@code false}
	 * 登录成功后不会再重置为{@code false}
	 */
	private volatile boolean logined;

	/**
	 * 玩家
	 */
	private BasePlayer player;

	public Session(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * 断开连接，解除ctx和Session、AbsPlayer和Session的绑定，一旦断开了连接，那么服务器将无法继续向玩家发送消息
	 * <p>
	 * 此处只是断开客户端-服务器的连接，但是实际上Session和AbsPlayer并没有被真正的从内存中移除
	 * <p>
	 * 通常连接断开后Session即将被移除
	 * <p>
	 * 为保证始终可以通过Session到玩家，玩家数据下线必须在Session彻底关闭后
	 */
	public void disconnect() {
		connect = false;
		if (player != null) {
			player.setSession(null);
		}
		player = null;
		if (ctx != null) {
			try {
				ctx.channel().attr(KeimonsHandler.SESSION).set(null);
				ctx.close();
			} catch (Exception e) {
				LogService.error(e, "关闭客户端-服务器连接失败");
			}
		}
		ctx = null;
		SessionManager.getInstance().removeSession(this);
		// Fixed 是否登录过是一个标识状态，哪怕连接断开了，也不能置为false
		// logined = false;
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
		if (connect && msg != null && ctx != null) {
			ctx.writeAndFlush(msg).addListener((GenericFutureListener<ChannelFuture>) future -> {
				// 发送失败，打印错误信息
				if (!future.isSuccess()) {
					LogService.error(future.cause());
				}
			});
		}
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public Session setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
		return this;
	}

	public boolean isConnect() {
		return connect;
	}

	public Session setConnect(boolean connect) {
		this.connect = connect;
		return this;
	}

	public boolean isLogined() {
		return logined;
	}

	public Session setLogined(boolean logined) {
		this.logined = logined;
		return this;
	}

	public BasePlayer getPlayer() {
		return player;
	}

	public Session setPlayer(BasePlayer player) {
		this.player = player;
		return this;
	}
}