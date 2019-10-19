package com.keimons.platform.module;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.CodeUtil;

import java.util.*;

/**
 * 玩家所有模块数据
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Modules {

	/**
	 * 数据唯一标识
	 */
	private final String identifier;

	/**
	 * 储存玩家所有数据
	 */
	private HashMap<String, IPlayerData> modules = new HashMap<>();

	/**
	 * 最后活跃时间
	 */
	protected volatile long lastTime;

	/**
	 * 构造函数
	 *
	 * @param identifier 数据唯一表示
	 */
	public Modules(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * 获取玩家的一个模块
	 *
	 * @param moduleName 模块名称
	 * @param <T>        模块类型
	 * @return 数据模块
	 */
	@SuppressWarnings("unchecked")
	public <T extends IPlayerData> T getModule(String moduleName) {
		IPlayerData data = modules.get(moduleName);
		if (data == null) {
			synchronized (this) {
				if (modules.get(moduleName) == null) {
					checkPlayerData();
				}
			}
			data = modules.get(moduleName);
		}
		if (data.getVersion() < KeimonsServer.VERSION) {
			synchronized (this) {
				if (data.getVersion() < KeimonsServer.VERSION) {
					byte[] oldVersionData = CodeUtil.encode(data);
					IPlayerData newVersionData = CodeUtil.decode(null, oldVersionData);
					modules.put(moduleName, newVersionData);
				}
			}
		}
		return (T) modules.get(moduleName);
	}

	/**
	 * 增加玩家数据
	 *
	 * @param data 玩家私有数据模块
	 */
	public void addPlayerData(IPlayerData data) {
		modules.put(data.getModuleName(), data);
	}

	/**
	 * 获取玩家所有的模块数据
	 *
	 * @return 玩家所有模块数据
	 */
	public Collection<IPlayerData> getModules() {
		return modules.values();
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
	public void checkPlayerData() {
		try {
			List<IPlayerData> init = new ArrayList<>();
			for (Map.Entry<String, Class<? extends IPlayerData>> entry : ModulesManager.classes.entrySet()) {
				if (!hasModule(entry.getKey())) {
					IPlayerData data = entry.getValue().newInstance();
					addPlayerData(data);
					init.add(data);
				}
			}
			for (IPlayerData data : init) {
				data.init(this);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			LogService.error(e);
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public long getLastTime() {
		return lastTime;
	}

	public Modules setLastTime(long lastTime) {
		this.lastTime = lastTime;
		return this;
	}
}