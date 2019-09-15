package com.keimons.platform.iface;

import com.keimons.platform.player.AbsPlayer;

/**
 * 玩家数据已经完全加载
 */
public interface ILoaded {

	/**
	 * 数据成功加载
	 *
	 * @param player 玩家
	 */
	void loaded(AbsPlayer player);
}