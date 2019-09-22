package com.keimons.platform.player;

import com.google.protobuf.MessageLite;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AModule;
import com.keimons.platform.iface.IModule;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.CharsetUtil;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.MD5Util;
import com.keimons.platform.unit.TimeUtil;

import java.util.*;
import java.util.function.Consumer;

/**
 * 玩家管理器
 */
public class ModuleManager {

	private static Map<Long, AbsPlayer> players = new HashMap<>();

	/**
	 * 系统中玩家的模块
	 */
	private static Set<Class<? extends IPlayerData>> modules = new HashSet<>();

	/**
	 * 加载并执行
	 *
	 * @param playerId 玩家ID
	 * @param consumer 消费函数
	 */
	public static void loadAndExecute(long playerId, Consumer<AbsPlayer> consumer) {
		AbsPlayer player = players.get(playerId);
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
	public static void loadAndExecute2(long playerId, Consumer<AbsPlayer> consumer) {
		AbsPlayer player = players.get(playerId);
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

	public static void main(String[] args) {
		String i = 5 * 1_000_000 + "";
		System.out.println(i.substring(0, 1));
	}

	/**
	 * 踢玩家下线
	 * 关闭Session -> 移除缓存 -> 保存玩家数据
	 *
	 * @param player   玩家
	 * @param kickType 1.异地登陆 2.强制下线
	 */
	public void kickPlayer(AbsPlayer player, int kickType) {
		if (player != null) {
			// TODO 玩家下线需要通知客户端下线类型
			player.kick();
		}
	}

	/**
	 * 保存所有玩家数据 如果玩家已经下线，则移除玩家
	 *
	 * @param coercive 是否强制存储
	 */
	public void saveAllPlayer(boolean coercive) {
		for (AbsPlayer player : players.values()) {
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
		for (AbsPlayer player : players.values()) {
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
		for (AbsPlayer player : players.values()) {
			player.send(msgCode, data);
		}
	}

	/**
	 * 持久化一个玩家
	 *
	 * @param player   玩家
	 * @param coercive 是否强制存储
	 */
	public boolean savePlayer(AbsPlayer player, boolean coercive) {
		if (player != null) {
			try {
				Map<byte[], byte[]> module = new HashMap<>();
				for (IPlayerData data : player.getModules()) {
					data.encode();
					byte[] bytes = DataUtil.encode(data);
					String md5 = MD5Util.md5(bytes);
					if (coercive || data.getLastMd5() == null || !data.getLastMd5().equals(md5)) {
						data.setLastMd5(md5);
						module.put(CharsetUtil.getUTF8(data.getModuleType().toString()), bytes);
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
	public static void loadPlayer(AbsPlayer player) {
		long time = TimeUtil.currentTimeMillis();
		int size = 0;
		if (player != null) {
			Map<byte[], byte[]> playerData = null; // RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(player.getPlayerId()));
			for (Map.Entry<byte[], byte[]> entry : playerData.entrySet()) {
				size += entry.getKey().length;
				size += entry.getValue().length;
				// 反序列化
				IPlayerData data = DataUtil.decode(CharsetUtil.getUTF8(entry.getKey()), entry.getValue());
				if (data != null) {
					data.decode();
					player.addPlayerData(data);
				}
			}
			// 玩家身上没有该模块，搞一个存进去
			DataUtil.checkPlayerData(player);
			for (IPlayerData data : player.getModules()) {
				data.loaded(player);
			}
		}
	}

	/**
	 * 加载一个玩家的数据
	 *
	 * @param playerId   玩家ID
	 * @param moduleType 模块
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IPlayerData> T loadPlayer(long playerId, Enum<? extends IModule> moduleType) {
		byte[] data = null; //RedissonManager.getMapValue(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(playerId), CharsetUtil.getUTF8(moduleType.toString()));
		// 反序列化
		IPlayerData module = DataUtil.decode(moduleType.toString(), data);
		if (module != null) {
			module.decode();
		}
		return (T) module;
	}

	public void init() {
		modules.addAll(ClassUtil.load(KeimonsServer.PackageName, AModule.class, IPlayerData.class));
	}

	public boolean shutdown() {
		saveAllPlayer(true);
		return false;
	}
}