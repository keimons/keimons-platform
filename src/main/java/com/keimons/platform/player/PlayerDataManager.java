package com.keimons.platform.player;

import com.google.protobuf.MessageLite;
import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.iface.IModule;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.CharsetUtil;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.CodeUtil;
import com.keimons.platform.unit.TimeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 玩家数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-10-02
 * @since 1.8
 */
public class PlayerDataManager {

	/**
	 * 玩家数据模块
	 */
	public static Map<String, Class<? extends IPlayerData>> modules = new HashMap<>();

	private static Map<Long, BasePlayer> players = new HashMap<>();

	/**
	 * 存储所有模块的当前版本号，这个版本号依赖于class文件的变动
	 * <p>
	 * 版本自动升级，一旦发现新加载上来的class文件有变动，则升级版本号
	 */
	private static Map<String, Integer> versions = new HashMap<>();

	/**
	 * 加载并执行
	 *
	 * @param playerId 玩家ID
	 * @param consumer 消费函数
	 */
	public static void loadAndExecute(long playerId, Consumer<BasePlayer> consumer) {
		BasePlayer player = players.get(playerId);
		if (player == null) {
//			Loader.fastLoad(new Player().setPlayerId(playerId), consumer);
		} else {
			if (consumer != null) {
				consumer.accept(player);
			}
		}
	}

	/**
	 * 加载并执行
	 *
	 * @param playerId 玩家ID
	 * @param consumer 消费函数
	 */
	public static void loadAndExecute2(long playerId, Consumer<BasePlayer> consumer) {
		BasePlayer player = players.get(playerId);
		if (player == null) {
//			Loader.slowLoad(new Player().setPlayerId(playerId), false, consumer);
		} else {
			if (consumer != null) {
				consumer.accept(player);
			}
		}
	}

	/**
	 * 创建一个PlayerId
	 *
	 * @param playerName 昵称
	 * @return 创建出来的玩家ID
	 */
	public synchronized long createPlayerId(String playerName) {
		if (playerName == null) {
			return 0L;
		}
		String hold = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
//		RMap<String, String> map = RedissonManager.getRedisson().getMap(RedisKeys.keyOfNameToPlayerId());
//		map.putIfAbsent(playerName, hold);
//		String result = map.get(playerName);
//		if (hold.equals(result)) {
//			long playerId = KeimonsServer.ServerId * KeimonsServer.MAX + RedissonManager.incrementAndGet(RedisKeys.keyOfPlayerId());
//			map.put(playerName, String.valueOf(playerId));
//			return playerId;
//		}
		return 0L;
	}

	/**
	 * 踢玩家下线
	 * 关闭Session -> 移除缓存 -> 保存玩家数据
	 *
	 * @param player   玩家
	 * @param kickType 1.异地登陆 2.强制下线
	 */
	public void kickPlayer(BasePlayer player, int kickType) {
		if (player != null) {
			// TODO 玩家下线需要通知客户端下线类型
		}
	}

	/**
	 * 保存所有玩家数据 如果玩家已经下线，则移除玩家
	 *
	 * @param coercive 是否强制存储
	 */
	public void saveAllPlayer(boolean coercive) {
		for (BasePlayer player : players.values()) {
			if (player.getSession() == null && TimeUtil.currentTimeMillis() - player.getLastActiveTime() > TimeUtil.minuteToMillis(5)) {
				if (savePlayer(player, true)) {
					player.getLock().lock();
					try {
						// 数据缓存4个小时
//						if (player.getRobotId() == 0 && TimeUtil.currentTimeMillis() - player.getLastActiveTime() > TimeUtil.houseToMillis(4)) {
//							offlinePlayer(player);
//						}
					} finally {
						player.getLock().unlock();
					}
				}
			} else {
				savePlayer(player, coercive);
			}
		}
	}

	/**
	 * 发送消息至所有在线玩家
	 *
	 * @param msgCode 消息号
	 * @param msg     消息体
	 */
	public void sendToOnlinePlayer(int msgCode, MessageLite msg) {
		for (BasePlayer player : players.values()) {
			player.send(msgCode, msg);
		}
	}

	/**
	 * 发送消息至所有在线玩家
	 *
	 * @param msgCode 消息号
	 * @param data    数据
	 */
	public void sendToOnlinePlayer(int msgCode, byte[] data) {
		for (BasePlayer player : players.values()) {
			player.send(msgCode, data);
		}
	}

	/**
	 * 持久化一个玩家
	 *
	 * @param player   玩家
	 * @param coercive 是否强制存储
	 */
	public boolean savePlayer(BasePlayer player, boolean coercive) {
		if (player != null) {
			try {
				Map<byte[], byte[]> module = new HashMap<>();
				for (IPlayerData data : player.getModules()) {
					data.encode();
					byte[] bytes = data.latest(coercive);
					if (bytes != null) {
						module.put(CharsetUtil.getUTF8(data.getModuleName()), bytes);
					}
				}
				if (!module.isEmpty()) {
//					RedissonManager.setMapValues(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(player.getPlayerId()), module);
				}
			} catch (Exception e) {
				LogService.error(e, "存储玩家数据失败");
				return false;
			}
		}
		return true;
	}

	/**
	 * 加载一个玩家的数据
	 *
	 * @param player 玩家
	 */
	public static void loadPlayer(BasePlayer player) {
		long time = TimeUtil.currentTimeMillis();
		int size = 0;
		if (player != null) {
			Map<byte[], byte[]> playerData = null; // RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(player.getPlayerId()));
			for (Map.Entry<byte[], byte[]> entry : playerData.entrySet()) {
				size += entry.getKey().length;
				size += entry.getValue().length;
				String moduleName = CharsetUtil.getUTF8(entry.getKey());
				// 反序列化
				IPlayerData data = CodeUtil.decode(modules.get(moduleName), entry.getValue());
				if (data != null) {
					data.decode();
					player.addPlayerData(data);
				}
			}
			// 玩家身上没有该模块，搞一个存进去
			player.checkPlayerData();
			for (IPlayerData data : player.getModules()) {
				data.loaded(player);
			}
		}
	}

	/**
	 * 加载一个玩家的数据
	 *
	 * @param playerId   玩家ID
	 * @param moduleName 模块
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IPlayerData> T loadPlayer(long playerId, Enum<? extends IModule> moduleName) {
		byte[] data = null; //RedissonManager.getMapValue(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(playerId), CharsetUtil.getUTF8(moduleType.toString()));
		// 反序列化
		Class<? extends IPlayerData> clazz = modules.get(moduleName.toString());
		IPlayerData module = CodeUtil.decode(clazz, data);
		if (module != null) {
			module.decode();
		}
		return (T) module;
	}

	/**
	 * 增加一个玩家数据模块
	 *
	 * @param packageName 包名
	 */
	public static void addPlayerData(String packageName) {
		List<Class<IPlayerData>> classes = ClassUtil.load(packageName, APlayerData.class);
		for (Class<IPlayerData> clazz : classes) {
			System.out.println("正在安装独有数据模块：" + clazz.getSimpleName());
			try {
				IPlayerData data = clazz.newInstance();
				String moduleName = data.getModuleName();
				modules.put(moduleName, clazz);
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e, "由于模块添加是由系统底层完成，所以需要提供默认构造方法，如有初始化操作，请重载init和loaded方法中");
				System.exit(0);
			}
			System.out.println("成功安装独有数据模块：" + clazz.getSimpleName());
		}
	}

	public boolean shutdown() {
		saveAllPlayer(true);
		return false;
	}
}