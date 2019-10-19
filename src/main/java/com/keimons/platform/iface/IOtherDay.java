package com.keimons.platform.iface;

import com.keimons.platform.player.IPlayer;

/**
 * 跨天刷新模块
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IOtherDay {

	/**
	 * 跨天刷新，每天00:00刷新
	 *
	 * @param player     玩家
	 * @param otherWeek  是否跨周
	 * @param otherMonth 是否跨月
	 */
	default void otherDay0(IPlayer player, boolean otherWeek, boolean otherMonth) {
	}

	/**
	 * 跨天刷新，每天05:00刷新
	 *
	 * @param player     玩家
	 * @param otherWeek  是否跨周
	 * @param otherMonth 是否跨月
	 */
	default void otherDay5(IPlayer player, boolean otherWeek, boolean otherMonth) {
	}
}