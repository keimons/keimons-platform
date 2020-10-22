package com.keimons.platform.session;

import com.keimons.platform.executor.ICommitterStrategy;
import com.keimons.platform.keimons.DefaultPlayer;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.KeimonsHandler;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.unit.TimeUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
public class Session implements ISession {

	/**
	 * 会话的自增ID
	 */
	private static final AtomicInteger sessionIndex = new AtomicInteger();

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
	private volatile boolean landed;

	/**
	 * 玩家
	 */
	private DefaultPlayer player;

	/**
	 * 消息号请求时间
	 */
	private Map<Integer, Long> requestTime = new HashMap<>();

	/**
	 * 会话唯一ID
	 */
	private final int sessionId;

	/**
	 * 构造方法
	 *
	 * @param ctx 客户端-服务器连接
	 */
	public Session(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.sessionId = sessionIndex.getAndIncrement();
	}

	/**
	 * 请求间隔的验证和更新
	 * <p>
	 * 服务器防刷的一道验证，同时也是防止某些，消耗（IO，CPU，RAM等）过大的消息频繁
	 * 的被请求，造成服务器资源被耗尽。如果客户端请求过于频繁，将抛弃这条消息，并返回
	 * 客户端消息过于频繁的错误号。这个间隔默认是50ms，也就是说，消息每秒最多被请求
	 * 20次，涉及消耗过大的操作，需要适当延长这个时间。
	 *
	 * @param msgCode  协议号
	 * @param timeNow  当前时间
	 * @param interval 间隔时间
	 * @return true.请求频率超过安全范围。false.请求频率在安全范围内。
	 */
	public boolean intervalVerifyAndUpdate(int msgCode, long timeNow, int interval) {
		long lastRequestTime = requestTime.getOrDefault(msgCode, TimeUtil.currentTimeMillis());
		requestTime.put(msgCode, timeNow);
		if (timeNow - lastRequestTime > interval) {
			return true;
		}
		return false;
	}

	@Override
	public Object getExecutorCode() {
		return player == null ? ICommitterStrategy.DEFAULT : player.getIdentifier();
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
	 * 发送消息
	 *
	 * @param msg 消息
	 * @param <T> 发送消息体
	 */
	public <T> void send(T msg) {
		if (connect && msg != null && ctx != null) {
			ctx.writeAndFlush(msg);
		}
	}

	public void lock() {
	}

	public void unlock() {
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	public boolean isConnect() {
		return connect;
	}

	public void setConnect(boolean connect) {
		this.connect = connect;
	}

	public boolean isLanded() {
		return landed;
	}

	public void setLanded(boolean landed) {
		this.landed = landed;
	}

	public DefaultPlayer getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IPlayer<?> player) {
		this.player = (DefaultPlayer) player;
	}

	public int getSessionId() {
		return sessionId;
	}
}