package com.keimons.platform.player;

import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 玩家数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class PlayerManager {

	/**
	 * 玩家数据模块
	 */
	public static Map<String, Class<? extends IGameData>> classes = new HashMap<>();

	/**
	 * 玩家数据
	 */
	private static Map<Object, IPlayer<?>> players = new HashMap<>();

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
	 * 创建并缓存模块数据
	 *
	 * @param identifier 玩家唯一标识符
	 * @param create     构造函数，如果没有找到这个对象，则构造这个对象
	 * @param <T>        玩家数据的唯一标识符的类型
	 * @return 模块数据
	 */
	public static <T> IPlayer findPlayer(T identifier, Function<T, IPlayer<T>> create) {
		@SuppressWarnings("unchecked")
		Function<Object, ? extends IPlayer<?>> _create =
				(Function<Object, IPlayer<T>>) create;
		IPlayer<?> player = players.computeIfAbsent(identifier, _create);
		player.checkModule();
		cacheModules(player);
		return player;
	}

	/**
	 * 加载并执行（异步）
	 *
	 * @param player   玩家
	 * @param consumer 消费函数
	 */
	public static void loadAndExecute(IPlayer player, Consumer<IPlayer> consumer) {
		PlayerLoader.slowLoad(player, (p) -> {
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
	public static IPlayer load(IPlayer player) {
		return PlayerLoader.fastLoad(player);
	}

	/**
	 * 加载一个玩家的数据，并且在加载完成之后对数据进行消费
	 *
	 * @param identifier 数据唯一标识符
	 * @param consumer   消费函数，加载成功后执行逻辑
	 */
	public static void loadModules(String identifier, Consumer<IPlayer<?>> consumer) {

	}

	/**
	 * 缓存玩家模块数据
	 *
	 * @param modules 所有模块数据
	 */
	public static void cacheModules(IPlayer modules) {
		PlayerManager.players.put(modules.getIdentifier(), modules);
	}

	/**
	 * 获取玩家的模块
	 *
	 * @param identifier 唯一标识符
	 * @param <T>        玩家数据的唯一标识符的类型
	 * @return 模块集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> IPlayer<T> getModules(T identifier) {
		return (IPlayer<T>) players.get(identifier);
	}

	/**
	 * 查找该包下的所有游戏数据结构
	 *
	 * @param packageName 包名
	 */
	public static void addGameData(String packageName) {
		List<Class<IGameData>> classes = ClassUtil.loadClasses(packageName, AGameData.class);
		for (Class<IGameData> clazz : classes) {
			AGameData annotation = clazz.getDeclaredAnnotation(AGameData.class);
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