package com.keimons.platform.iface;

import com.keimons.platform.player.IPlayer;

/**
 * 玩家数据加载成功接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface ILoaded {

	/**
	 * 数据成功加载
	 *
	 * @param player 玩家
	 * @param <T>    玩家类型
	 */
	<T extends IPlayer> void loaded(T player);
}