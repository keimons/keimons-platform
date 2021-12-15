package com.keimons.nutshell.def;

import com.keimons.nutshell.module.BaseSystem;

/**
 * 游戏公共数据管理器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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