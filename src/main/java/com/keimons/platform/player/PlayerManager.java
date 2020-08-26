package com.keimons.platform.player;

import com.keimons.platform.module.IGameData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 玩家数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class PlayerManager {

	/**
	 * 空模块
	 */
	@SuppressWarnings("unchecked")
	public static final Class<? extends IGameData>[] EMPTY_MODULES = (Class<? extends IGameData>[]) new Class<?>[0];

	/**
	 * 玩家数据模块
	 */
	public static Map<String, Class<? extends IPlayerData>> classes = new HashMap<>();

	/**
	 * 玩家数据
	 */
	private static ConcurrentHashMap<Object, IPlayer<?>> players = new ConcurrentHashMap<>();

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
		for (IPlayer<?> baseModules : players.values()) {
			persistence(baseModules, coercive);
		}
	}

	/**
	 * 持久化一个玩家
	 *
	 * @param player   玩家
	 * @param coercive 是否强制存储
	 */
	public void persistence(IPlayer<?> player, boolean coercive) {
		player.save(coercive);
	}

	/**
	 * 查找玩家
	 *
	 * @param <T>        主键类型
	 * @param <R>        玩家类型
	 * @param identifier 主键
	 * @param modules    模块
	 * @return 玩家
	 */
	@SafeVarargs
	public static <T, R extends IPlayer<T>> R findPlayer(T identifier, Class<? extends IPlayerData>... modules) {
		return load(identifier, null, modules, true, null);
	}

	/**
	 * 创建并缓存模块数据
	 *
	 * @param identifier 玩家唯一标识符
	 * @param create     构造函数，如果没有找到这个对象，则构造这个对象
	 * @param modules    需要的模块
	 * @param <T>        玩家主键类型
	 * @param <R>        玩家类型
	 * @return 模块数据
	 */
	@SafeVarargs
	public static <T, R extends IPlayer<T>> R findPlayer(T identifier, Function<T, IPlayer<T>> create, Class<? extends IPlayerData>... modules) {
		return load(identifier, create, modules, true, null);
	}

	/**
	 * 创建并缓存模块数据
	 *
	 * @param identifier 玩家唯一标识符
	 * @param create     构造函数，如果没有找到这个对象，则构造这个对象
	 * @param consumer   消耗函数加载完成后对应的操作
	 * @param modules    需要加载的模块
	 * @param <T>        玩家主键类型
	 * @param <R>        玩家类型
	 */
	@SafeVarargs
	public static <T, R extends IPlayer<T>> void findPlayer(T identifier, Function<T, IPlayer<T>> create, Consumer<R> consumer, Class<? extends IPlayerData>... modules) {
		load(identifier, create, modules, false, consumer);
	}

	/**
	 * 加载玩家到内存
	 *
	 * @param identifier 主键
	 * @param fast       快速加载
	 * @param create     创建函数
	 * @param modules    模块
	 * @param consumer   消耗函数
	 * @param <T>        玩家主键class类型
	 * @param <R>        玩家class类型
	 * @return 玩家
	 */
	@SuppressWarnings("unchecked")
	public static <T, R extends IPlayer<T>> R load(T identifier, Function<T, IPlayer<T>> create, Class<? extends IPlayerData>[] modules, boolean fast, Consumer<R> consumer) {
		if (create == null) {
			R player = (R) players.get(identifier);
			if (player == null) {
				return null;
			}
			if (modules.length == 0) {
				return player.isLoaded() ? player : null;
			}
			if (player.hasModules(modules)) {
				return player;
			}
			return null;
		}
		BiFunction<Object, IPlayer<?>, IPlayer<?>> create0 = (key, player) -> {
			if (player == null) {
				player = create.apply(identifier);
			}
			player.load(modules);
			player.loaded();
			player.setActiveTime(TimeUtil.currentTimeMillis());
			if (modules.length == 0) {
				player.setLoaded(true);
			}
			return player;
		};
		if (fast) {
			return (R) players.compute(identifier, create0);
		} else {
			PlayerLoader.slowLoad(() -> {
				try {
					R player = (R) players.compute(identifier, create0);
					if (consumer != null) {
						consumer.accept(player);
					}
				} catch (Exception e) {
					LogService.error(e);
				}
			});
			return null;
		}
	}

	/**
	 * 移除玩家
	 *
	 * @param identifier 唯一标识符
	 * @param predicate  移除规则
	 * @param modules    要加载的模块
	 * @param <T>        玩家数据唯一标识符类型
	 * @param <R>        玩家类型
	 * @return 被移除的玩家
	 */
	@SafeVarargs
	public static <T, R extends IPlayer<T>> R removePlayer(T identifier, Predicate<IPlayer<?>> predicate, Class<? extends IPlayerData>... modules) {
		return unload(identifier, predicate, modules);
	}

	/**
	 * 卸载玩家
	 *
	 * @param identifier 玩家唯一标识符
	 * @param predicate  移除规则 {@code true} 移除 {@code false} 不移除
     * @param modules    保留模块
     * @param <T>        玩家主键类型
     * @param <R>        玩家类型
     * @return 被卸载的玩家
	 */
	@SuppressWarnings("unchecked")
	public static <T, R extends IPlayer<T>> R unload(T identifier, Predicate<IPlayer<?>> predicate, Class<? extends IPlayerData>[] modules) {
		BiFunction<Object, IPlayer<?>, IPlayer<?>> create = (playerId, player) -> {
			if (player == null) {
				return null;
			}
			if (predicate.test(player)) {
				if (modules.length == 0) {
					return null;
				} else {
					player.unloadIfNot(modules);
				}
			}
			return player;
		};
		return (R) players.compute(identifier, create);
	}

	/**
	 * 查找该包下的所有游戏数据结构
	 *
	 * @param packageName 包名
	 */
	public static void addGameData(String packageName) {
		List<Class<IPlayerData>> classes = ClassUtil.loadClasses(packageName, APlayerData.class);
		for (Class<IPlayerData> clazz : classes) {
			APlayerData annotation = clazz.getDeclaredAnnotation(APlayerData.class);
			PlayerManager.classes.put(annotation.moduleName(), clazz);
			System.out.println("查找到数据结构：" + annotation.moduleName() +
					"，是否压缩：" + annotation.isCompress());
		}
	}

	public static void init() {
		Executor single = Executors.newSingleThreadExecutor();
		single.execute(new PlayerLoader());
	}

	public boolean shutdown() {
		persistence(true);
		return false;
	}
}