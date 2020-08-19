package com.keimons.platform.module;

import com.keimons.platform.annotation.ASystemData;
import com.keimons.platform.iface.IGameData;

import java.util.concurrent.ConcurrentHashMap;

public class SystemData {

	/**
	 * 系统数据 Key:数据名称 Value:数据
	 */
	protected static final ConcurrentHashMap<String, IModule<? extends ISystemData>> modules = new ConcurrentHashMap<>();

	/**
	 * 获取游戏共有数据
	 *
	 * @param clazz 数据模块
	 * @param <T>   模块实现类
	 * @return 模块数据
	 */
//	@SuppressWarnings("unchecked")
	public static <T extends ISingularSystemData> T get(Class<T> clazz) {
		ASystemData annotation = clazz.getAnnotation(ASystemData.class);
		String moduleName = annotation.moduleName();
		ISingularModule<T> module = (ISingularModule<T>) modules.get(moduleName);
//		if (module == null) {
//			synchronized (SystemData.class) {
//				module = modules.computeIfAbsent(clazz, value -> {
//					try {
//						return clazz.getDeclaredConstructor().newInstance();
//					} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//						LogService.error(e, "模块加载失败，未找到默认的构造方法！");
//					}
//					return null;
//				});
//			}
//		}
		return module.get();
	}

	/**
	 * 增加一个游戏模块数据
	 *
	 * @param data 共有数据
	 */
	public void addSystemData(IGameData data) {
//		modules.put(data.getClass(), data);
	}
}