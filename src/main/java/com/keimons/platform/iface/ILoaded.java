package com.keimons.platform.iface;

import com.keimons.platform.player.AbsPlayer;

/**
 * 玩家数据加载成功接口
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
public interface ILoaded {

	/**
	 * 数据成功加载
	 *
	 * @param player 玩家
	 */
	void loaded(AbsPlayer player);
}