package com.keimons.platform.player;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.keimons.platform.annotation.AModule;
import com.keimons.platform.iface.IData;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO转化为byte[]工具
 */
public class ModuleUtil {

	/**
	 * 整个jar包中所有的ProtoBuf对象
	 */
	private static Map<Class<? extends IData>, Codec<IData>> codecs = new HashMap<>();

	/**
	 * 玩家数据
	 */
	private static Map<String, Class<? extends IPlayerData>> modules = new HashMap<>();

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public static void init(String packageName) {
		List<Class<IPlayerData>> classes = ClassUtil.load(packageName, AModule.class, null);
		for (Class<IPlayerData> clazz : classes) {
			System.out.println("玩家数据模块：" + clazz.getName());
			// 由程序自行缓存，关闭工具的自带缓存
			ProtobufProxy.enableCache(false);
			Codec simpleTypeCodec = ProtobufProxy.create(clazz, true);
			codecs.put(clazz, simpleTypeCodec);
			IPlayerData data = null;
			try {
				data = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e, "由于模块添加是由系统底层完成，所以需要提供默认构造方法，如有初始化操作，请重载init和loaded方法中");
				System.exit(0);
			}
			try {
				String moduleName = (String) clazz.getMethod("getModuleName").invoke(data);
				modules.put(moduleName, clazz);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LogService.error(e, "未能成功获取模块类型");
			}
		}
	}

	/**
	 * 序列化
	 * 将ProtoBuf对象序列化为byte数组
	 *
	 * @param data 数据
	 * @return 序列化后的数据
	 */
	public static byte[] encode(IData data) {
		try {
			return codecs.get(data.getClass()).encode(data);
		} catch (IOException e) {
			LogService.error(e, "序列化错误！");
		}
		return null;
	}

	/**
	 * 反序列化
	 * 将byte数组反序列化为Java对象
	 *
	 * @param clazz 要被解析的类
	 * @param data  要被解析的数据
	 * @param <T>   泛型类型
	 * @return Java对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IData> T decode(Class<T> clazz, byte[] data) {
		try {
			Codec<T> codec = (Codec<T>) codecs.get(clazz);
			if (codec == null) {
				return null;
			}
			return codec.decode(data);
		} catch (IOException e) {
			LogService.error(e, "数据解析错误");
		}
		return null;
	}

	/**
	 * 反序列化
	 * 将String反序列化为Java对象
	 *
	 * @param clazz 要被解析的类
	 * @param data  要被解析的数据
	 * @param <T>   泛型类型
	 * @return Java对象
	 */
	public static <T extends IData> T decode(Class<T> clazz, String data) {
		return decode(clazz, data.getBytes(Charset.forName("ISO-8859-1")));
	}

	/**
	 * 查找模块Class文件
	 *
	 * @param moduleName 模块名称
	 * @return 模块Class文件
	 */
	public static Class<? extends IPlayerData> getModuleClass(String moduleName) {
		return modules.get(moduleName);
	}

	/**
	 * 返回所有玩家数据模块
	 *
	 * @return 所有玩家数据模块
	 */
	public static Map<String, Class<? extends IPlayerData>> getModules() {
		return modules;
	}
}