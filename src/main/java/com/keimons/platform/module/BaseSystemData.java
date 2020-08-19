package com.keimons.platform.module;

import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.annotation.ASystemData;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.log.LogService;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class BaseSystemData {

	/**
	 * 系统数据 Key:数据名称 Value:数据
	 */
	protected final ConcurrentHashMap<String, IModule<? extends IGameData>> modules = new ConcurrentHashMap<>();

	/**
	 * 获取玩家的一个模块
	 * <p>
	 * 如果玩家已经被完全加载，如果缺少这个模块，则补充这个模块，如果玩家没有被完全加载，则不能
	 *
	 * @param clazz 模块名称
	 * @param <V>   玩家数据类型
	 * @return 数据模块
	 */
	@SuppressWarnings("unchecked")
	public <V extends ISingularSystemData> V get(Class<V> clazz) {
		APlayerData annotation = clazz.getAnnotation(APlayerData.class);
		String moduleName = annotation.moduleName();
		ISingularModule<V> module = (ISingularModule<V>) modules.get(moduleName);
		if (module == null && !modules.containsKey(moduleName)) {
			synchronized (this) {
				if (!modules.containsKey(moduleName)) {
					try {
						ISingularSystemData data = clazz.getDeclaredConstructor().newInstance();
						addSingularData(data);
					} catch (Exception e) {
						LogService.error(e);
					}
				}
			}
			module = (ISingularModule<V>) modules.get(moduleName);
		}
		if (module == null) {
			return null;
		}
		return module.get();
	}

	/**
	 * 获取玩家的一个模块
	 *
	 * @param clazz  模块名称
	 * @param dataId 数据唯一IDs
	 * @param <V>    模块类型
	 * @return 数据模块
	 */
	@SuppressWarnings("unchecked")
	public <K, V extends IRepeatedSystemData<K>> V get(Class<V> clazz, K dataId) {
		ASystemData annotation = clazz.getAnnotation(ASystemData.class);
		String moduleName = annotation.moduleName();
		IRepeatedModule<K, V> module = (IRepeatedModule<K, V>) modules.get(moduleName);
		if (module == null) {
			return null;
		}
		return module.get(dataId);
	}

	/**
	 * 移除玩家的一个模块
	 *
	 * @param clazz  模块名称
	 * @param dataId 数据唯一IDs
	 * @param <V>    模块类型
	 * @return 数据模块
	 */
	@SuppressWarnings("unchecked")
	public <K, V extends IRepeatedPlayerData<K>> V remove(Class<V> clazz, K dataId) {
		ASystemData annotation = clazz.getAnnotation(ASystemData.class);
		String moduleName = annotation.moduleName();
		IRepeatedModule<K, V> module = (IRepeatedModule<K, V>) modules.get(moduleName);
		return module.remove(dataId);
	}

	/**
	 * 获取数据模块
	 *
	 * @param moduleName 模块名字
	 * @param function   新建模块
	 * @param <V>        模块类型
	 * @return 模块
	 */
	@SuppressWarnings("unchecked")
	protected <V extends IModule<? extends ISystemData>> V computeIfAbsent(
			String moduleName, Function<String, V> function) {
		Objects.requireNonNull(function);
		return (V) modules.computeIfAbsent(moduleName, function);
	}

	/**
	 * 增加一个可重复的模块数据
	 *
	 * @param data 数据
	 */
	public abstract <K> void addRepeatedData(IRepeatedPlayerData<K> data);

	/**
	 * 增加一个非重复的模块数据
	 *
	 * @param data 数据
	 */
	public abstract void addSingularData(ISingularSystemData data);

	/**
	 * 查找是否有这个模块
	 *
	 * @param classes 类
	 * @return 是否包含这些模块
	 */
	@SafeVarargs
	public final boolean hasModules(Class<? extends ISystemData>... classes) {
		for (Class<? extends ISystemData> clazz : classes) {
			ASystemData annotation = clazz.getAnnotation(ASystemData.class);
			if (annotation == null) {
				return false;
			}
			if (!modules.containsKey(annotation.moduleName())) {
				return false;
			}
		}
		return true;
	}
}