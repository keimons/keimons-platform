package com.keimons.platform.keimons;

import com.keimons.platform.module.BaseSystem;

/**
 * 游戏公共数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class SystemDataManager {

	/**
	 * 玩家数据模块
	 */
	private static BaseSystem systems = new DefaultSystem();

	public static BaseSystem getSystems() {
		return systems;
	}
}