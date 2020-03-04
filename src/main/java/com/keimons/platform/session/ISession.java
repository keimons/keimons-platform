package com.keimons.platform.session;

import com.keimons.platform.player.IPlayer;

/**
 * 会话接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface ISession {

	/**
	 * 提交消息
	 *
	 * @param packet 消息体
	 */
	void commit(Object packet);

	/**
	 * 完成消息执行
	 */
	void finish();

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 获取这个session对应的玩家
	 *
	 * @return 玩家
	 */
	IPlayer<?> getPlayer();

	/**
	 * 设置这个Session对应的玩家
	 *
	 * @param player 玩家
	 */
	void setPlayer(IPlayer<?> player);

	/**
	 * 获取Session唯一Id
	 *
	 * @return Session唯一Id
	 */
	int getSessionId();

	/**
	 * 获取Session的上次活跃时间
	 *
	 * @return 上次活跃时间
	 */
	long getLastActiveTime();
}