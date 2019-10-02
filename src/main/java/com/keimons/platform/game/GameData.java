package com.keimons.platform.game;

import com.keimons.platform.iface.IGameData;
import com.keimons.platform.log.LogService;

import java.util.HashMap;
import java.util.Map;

public enum GameData {

	/**
	 * 单例模式
	 */
	INSTANCE;

	/**
	 * 所有游戏公共数据
	 */
	private Map<Class<?>, IGameData> modules = new HashMap<>();

	/**
	 * 获取游戏共有数据
	 *
	 * @param clazz 数据模块
	 * @param <T>   模块实现类
	 * @return 模块数据
	 */
	@SuppressWarnings("unchecked")
	public <T extends IGameData> T getGameData(Class<IGameData> clazz) {
		IGameData data = modules.get(clazz);
		if (data == null) {
			synchronized (this) {
				data = modules.computeIfAbsent(clazz, value -> {
					try {
						return clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						LogService.error(e, "模块加载失败，未找到默认的构造方法！");
					}
					return null;
				});
			}
		}
		return (T) data;
	}

	/**
	 * 增加一个游戏模块数据
	 *
	 * @param data 共有数据
	 */
	public void addGameData(IGameData data) {
		modules.put(data.getClass(), data);
	}
}