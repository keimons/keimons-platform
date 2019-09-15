package com.keimons.platform.iface;

import com.keimons.platform.player.AbsPlayer;

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