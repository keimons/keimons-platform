package com.keimons.platform.iface;

import com.keimons.platform.player.AbsPlayer;

/**
 * 跨天刷新模块
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
public interface IOtherDay {

	/**
	 * 跨天刷新 0点
	 * 每天00:00刷新
	 */
	default void otherDay0(AbsPlayer player, boolean otherWeek, boolean otherMonth) {
	}

	/**
	 * 跨天刷新 5点
	 * 每天05:00刷新
	 */
	default void otherDay5(AbsPlayer player, boolean otherWeek, boolean otherMonth) {
	}
}