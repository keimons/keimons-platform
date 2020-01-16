package com.keimons.platform.module;

import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.iface.IModule;
import com.keimons.platform.iface.IRepeated;
import com.keimons.platform.log.LogService;
import com.keimons.platform.player.BytesPersistenceModule;
import com.keimons.platform.unit.CodeUtil;
import com.keimons.platform.unit.TimeUtil;
import org.redisson.client.codec.ByteArrayCodec;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家所有模块数据
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Modules implements IPersistence {

	public static final long DEFAULT_MODULE_KEY = 0L;

	/**
	 * 数据唯一标识
	 * <p>
	 * 当从数据库取出数据时，需要使用唯一标识符获取数据，当对数据进行持久化时，需要使用唯一标识符作为
	 * 主键，将数据存入数据库。
	 */
	private final String identifier;

	/**
	 * 玩家数据 Key:数据名称 Value:数据
	 */
	private Map<String, Map<Object, IModule>> modules = new ConcurrentHashMap<>();

	/**
	 * 最后活跃时间
	 */
	protected volatile long activeTime = TimeUtil.currentTimeMillis();

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
	 * @param clazz 模块名称
	 * @param <T>   模块类型
	 * @return 数据模块
	 */
	@SuppressWarnings("unchecked")
	public <T extends IModule> T getModule(Class<T> clazz, Object moduleKey) {
		APlayerData annotation = clazz.getAnnotation(APlayerData.class);
		boolean repeated = IRepeated.class.isAssignableFrom(clazz);
		String moduleName = annotation.name();
		Map<Object, IModule> module = modules.get(moduleName);
		if (module == null) {
			if (repeated) {
				return null;
			}
			synchronized (this) {
				if (modules.get(annotation.name()) == null) {
					check();
				}
				module = modules.get(moduleName);
			}
		}
		if (repeated) {
			return (T) module.get(moduleKey);
		} else {
			return (T) module.get(DEFAULT_MODULE_KEY);
		}
	}

	/**
	 * 增加玩家数据
	 *
	 * @param data 玩家私有数据模块
	 */
	public void addPlayerData(IModule data) {
		APlayerData annotation = data.getClass().getAnnotation(APlayerData.class);
		Map<Object, IModule> module = modules.computeIfAbsent(annotation.name(), v -> new ConcurrentHashMap<>());
		if (data instanceof IRepeated) {
			module.put(((IRepeated) data).getDataId(), data);
		} else {
			module.put(DEFAULT_MODULE_KEY, data);
		}
	}

	/**
	 * 获取玩家所有的模块数据
	 *
	 * @return 玩家所有模块数据
	 */
	public Map<String, Map<Object, IModule>> getModules() {
		return modules;
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
	public void check() {
		try {
			List<IModule> init = new ArrayList<>();
			for (Map.Entry<String, Class<? extends IModule>> entry : ModulesManager.classes.entrySet()) {
				// 判断模块是否需要被添加
				Class<? extends IModule> clazz = entry.getValue();
				if (!hasModule(entry.getKey()) && !IRepeated.class.isAssignableFrom(clazz)) {
					IModule data = clazz.newInstance();
					addPlayerData(data);
					init.add(data);
				}
			}
			for (IModule data : init) {
				data.init(this);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			LogService.error(e);
		}
	}

	public String getIdentifier() {
		return identifier;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	@Override
	public void save(boolean coercive) {
		Map<byte[], byte[]> bytes = new HashMap<>(modules.size());
		for (Map.Entry<String, Map<Object, IModule>> entry : modules.entrySet()) {
			BytesPersistenceModule dataModule = new BytesPersistenceModule();
			for (IModule data : entry.getValue().values()) {
				if (data instanceof IPersistence) {
					byte[] persistence = ((IBytesPersistence) data).persistence(coercive);
					if (persistence != null) {
						dataModule.module.add(persistence);
					}
				}
			}
			bytes.put(entry.getKey().getBytes(Charset.forName("UTF-8")), dataModule.getData());
		}
		if (bytes.size() > 0) {
			RedissonManager.setMapValues(ByteArrayCodec.INSTANCE, identifier, bytes);
		}
	}

	@Override
	public void load(Map<byte[], byte[]> map) {
		for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
			String moduleName = new String(entry.getKey(), Charset.forName("UTF-8"));
			Map<Object, IModule> module = modules.computeIfAbsent(moduleName, v -> new ConcurrentHashMap<>());
			BytesPersistenceModule decode = CodeUtil.decode(BytesPersistenceModule.class, entry.getValue());
			Class<? extends IModule> clazz = ModulesManager.classes.get(moduleName);
			for (byte[] bytes : decode.getModule()) {
				IModule data = CodeUtil.decode(clazz, bytes);
				if (IRepeated.class.isAssignableFrom(clazz)) {
					module.put(((IRepeated) data).getDataId(), data);
				} else {
					module.put(DEFAULT_MODULE_KEY, data);
				}
			}
		}
	}
}