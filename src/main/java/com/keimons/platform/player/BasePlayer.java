package com.keimons.platform.player;

import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.module.IModule;
import com.keimons.platform.module.IRepeatedModule;
import com.keimons.platform.module.ISingularModule;
import com.keimons.platform.unit.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 玩家基类
 *
 * @param <T> 玩家标识类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BasePlayer<T> implements IPlayer<T> {

	/**
	 * 数据唯一标识
	 * <p>
	 * 当从数据库取出数据时，需要使用唯一标识符获取数据，当对数据进行持久化时，需要使用唯一标识符作为
	 * 主键，将数据存入数据库。
	 */
	protected T identifier;

	/**
	 * 玩家数据 Key:数据名称 Value:数据
	 */
	protected final ConcurrentHashMap<String, IModule<? extends IGameData>> modules = new ConcurrentHashMap<>();

	/**
	 * 最后活跃时间
	 */
	private volatile long activeTime = TimeUtil.currentTimeMillis();

	/**
	 * 默认构造函数
	 */
	public BasePlayer() {
	}

	/**
	 * 构造函数
	 *
	 * @param identifier 数据唯一表示
	 */
	public BasePlayer(T identifier) {
		this.identifier = identifier;
	}

	/**
	 * 获取玩家的一个模块
	 *
	 * @param clazz 模块名称
	 * @param <V>   玩家数据类型
	 * @return 数据模块
	 */
	public <V extends ISingularPlayerData> V get(Class<V> clazz) {
		AGameData annotation = clazz.getAnnotation(AGameData.class);
		String moduleName = annotation.moduleName();
		ISingularModule<?> module = (ISingularModule<?>) modules.get(moduleName);
		return (V) module.get();
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
	public <V extends IRepeatedPlayerData> V get(Class<V> clazz, Object dataId) {
		AGameData annotation = clazz.getAnnotation(AGameData.class);
		String moduleName = annotation.moduleName();
		IRepeatedModule<?> module = (IRepeatedModule<?>) modules.get(moduleName);
		return (V) module.get(dataId);
	}

	@Override
	public T getIdentifier() {
		return this.identifier;
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
	public <V extends IRepeatedPlayerData> V remove(Class<V> clazz, Object dataId) {
		AGameData annotation = clazz.getAnnotation(AGameData.class);
		String moduleName = annotation.moduleName();
		IRepeatedModule<?> module = (IRepeatedModule<?>) modules.get(moduleName);
		return (V) module.remove(dataId);
	}

	@Override
	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	@Override
	public long getActiveTime() {
		return activeTime;
	}

	/**
	 * 增加一个可重复的模块数据
	 *
	 * @param data 数据
	 */
	public abstract void addRepeatedData(IRepeatedPlayerData data);

	/**
	 * 增加一个非重复的模块数据
	 *
	 * @param data 数据
	 */
	public abstract void addSingularData(ISingularPlayerData data);

	/**
	 * 获取数据模块
	 *
	 * @param moduleName 模块名字
	 * @param function   新建模块
	 * @param <V>        模块类型
	 * @return 模块
	 */
	@SuppressWarnings("unchecked")
	protected <V extends IModule<? extends IGameData>> V computeIfAbsent(
			String moduleName, Function<String, V> function) {
		Objects.requireNonNull(function);
		return (V) modules.computeIfAbsent(moduleName, function);
	}

	/**
	 * 检查玩家是否有该模块
	 *
	 * @param clazz 模块
	 * @return 是否有该模块
	 */
	public boolean hasModule(String clazz) {
		return modules.containsKey(clazz);
	}

	/**
	 * 检测玩家缺少的数据模块并添加该模块
	 */
	public void checkModule() {
		try {
			List<ISingularPlayerData> init = new ArrayList<>();
			for (Map.Entry<String, Class<? extends IGameData>> entry : PlayerManager.classes.entrySet()) {
				// 判断模块是否需要被添加
				Class<? extends IGameData> clazz = entry.getValue();
				if (!hasModule(entry.getKey()) && ISingularPlayerData.class.isAssignableFrom(clazz)) {
					ISingularPlayerData data = (ISingularPlayerData) clazz.newInstance();
					addSingularData(data);
					init.add(data);
				}
			}
			for (ISingularPlayerData data : init) {
				data.init(this);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			LogService.error(e);
		}
	}
}
