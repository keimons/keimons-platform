package com.keimons.platform.module;

import com.keimons.platform.player.APlayerData;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.JProtobufUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏公共数据管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class GameDataManager {

	/**
	 * 玩家数据模块
	 */
	private static Map<String, Class<? extends IGameData>> modules = new HashMap<>();

	/**
	 * 存储所有模块的当前版本号，这个版本号依赖于class文件的变动
	 * <p>
	 * 版本自动升级，一旦发现新加载上来的class文件有变动，则升级版本号
	 */
	private static Map<String, Integer> versions = new HashMap<>();

	/**
	 * 加载一个玩家的数据
	 *
	 * @param moduleName 模块
	 * @param <T>        模块类型
	 * @return 玩家数据
	 * @throws IOException 反序列化异常
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IGameData> T loadGameData(String moduleName) throws IOException {
		byte[] data = null; //RedissonManager.getMapValue(ByteArrayCodec.INSTANCE, RedisKeys.keyOfPlayerData(playerId), CharsetUtil.getUTF8(moduleType.toString()));
		// 反序列化
		Class<? extends IGameData> clazz = modules.get(moduleName);
		IGameData module = JProtobufUtil.decode(clazz, data);
		return (T) module;
	}

	/**
	 * 增加游戏模块
	 *
	 * @param packageName 包名
	 */
	public static void addGameData(String packageName) {
		List<Class<IGameData>> classes = ClassUtil.loadClasses(packageName, APlayerData.class);
		for (Class<IGameData> clazz : classes) {
			System.out.println("正在安装共有数据模块：" + clazz.getSimpleName());
			APlayerData annotation = clazz.getAnnotation(APlayerData.class);
			modules.put(annotation.moduleName(), clazz);
			System.out.println("成功安装共有数据模块：" + clazz.getSimpleName());
		}
	}
}