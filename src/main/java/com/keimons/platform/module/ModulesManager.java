package com.keimons.platform.module;

import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 玩家数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class ModulesManager {

	/**
	 * 玩家数据模块
	 */
	public static Map<String, Class<? extends IGameData>> classes = new HashMap<>();

	/**
	 * 玩家数据
	 */
	private static Map<Object, BaseModules<?>> modules = new HashMap<>();

	/**
	 * 存储所有模块的当前版本号，这个版本号依赖于class文件的变动
	 * <p>
	 * 版本自动升级，一旦发现新加载上来的class文件有变动，则升级版本号
	 */
	private static Map<String, Integer> versions = new HashMap<>();

	/**
	 * 保存所有玩家数据 如果玩家已经下线，则移除玩家
	 *
	 * @param coercive 是否强制存储
	 */
	public void persistence(boolean coercive) {
		for (BaseModules<?> baseModules : modules.values()) {
			persistence(baseModules, coercive);
		}
	}

	/**
	 * 持久化一个玩家
	 *
	 * @param baseModules 玩家
	 * @param coercive    是否强制存储
	 */
	public void persistence(BaseModules<?> baseModules, boolean coercive) {
		baseModules.save(coercive);
	}

	/**
	 * 创建并缓存模块数据
	 *
	 * @param player          玩家
	 * @param mappingFunction 构造函数
	 * @param <T>             玩家数据的唯一标识符的类型
	 * @return 模块数据
	 */
	public static <T> BaseModules createModules(IPlayer<T> player, Supplier<? extends BaseModules<T>> mappingFunction) {
		BaseModules<T> baseModules = mappingFunction.get();
		baseModules.checkModule();
		player.setModules(baseModules);
		cacheModules(baseModules);
		return baseModules;
	}

	/**
	 * 加载并执行（异步）
	 *
	 * @param player   玩家
	 * @param consumer 消费函数
	 */
	public static void loadAndExecute(IPlayer player, Consumer<IPlayer> consumer) {
		Loader.slowLoad(player, (p) -> {
			if (consumer != null) {
				consumer.accept(p);
			}
		});
	}

	/**
	 * 加载所有模块
	 *
	 * @param player 玩家
	 * @return 玩家所有模块
	 */
	public static BaseModules load(IPlayer player) {
		return Loader.fastLoad(player);
	}

	/**
	 * 加载一个玩家的数据，并且在加载完成之后对数据进行消费
	 *
	 * @param identifier 数据唯一标识符
	 * @param consumer   消费函数，加载成功后执行逻辑
	 */
	public static void loadModules(String identifier, Consumer<BaseModules<?>> consumer) {

	}

	/**
	 * 缓存玩家模块数据
	 *
	 * @param modules 所有模块数据
	 */
	public static void cacheModules(BaseModules modules) {
		ModulesManager.modules.put(modules.getIdentifier(), modules);
	}

	/**
	 * 获取玩家的模块
	 *
	 * @param identifier 唯一标识符
	 * @param <T>        玩家数据的唯一标识符的类型
	 * @return 模块集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> BaseModules<T> getModules(T identifier) {
		return (BaseModules<T>) modules.get(identifier);
	}

	/**
	 * 增加一个玩家数据模块
	 *
	 * @param packageName 包名
	 */
	public static void addPlayerData(String packageName) {
		List<Class<IGameData>> classes = ClassUtil.loadClasses(packageName, AGameData.class);
		for (Class<IGameData> clazz : classes) {
			System.out.println("正在安装独有数据模块：" + clazz.getSimpleName());
			String moduleName = clazz.getDeclaredAnnotation(AGameData.class).moduleName();
			ModulesManager.classes.put(moduleName, clazz);
			System.out.println("成功安装独有数据模块：" + clazz.getSimpleName());
		}
	}

	public static void init() {
		Executor single = Executors.newSingleThreadExecutor();
		single.execute(new Loader());
	}

	public boolean shutdown() {
		persistence(true);
		return false;
	}
}