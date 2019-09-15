package com.keimons.platform.player;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.keimons.platform.iface.BasePlayerData;
import com.keimons.platform.iface.IData;
import com.keimons.platform.iface.IModule;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.*;

/**
 * POJO转化为byte[]工具
 */
public class DataUtil {

	/**
	 * 整个jar包中所有的ProtoBuf对象
	 */
	private static Map<String, Codec> codecs = new HashMap<>();

	private static Map<String, String> module = new HashMap<>();

	/**
	 * 玩家数据
	 */
	private static Set<Class<? extends IPlayerData>> modules = new HashSet<>();

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public static void init(String packageName) {
		try {
			// 查找指定packet下所有class文件
			Set<Class<?>> classes = ClassUtil.getClasses(packageName);
			for (Class<?> clazz : classes) {
				// 这个class实现了IPlayerData接口
				if (IPlayerData.class.isAssignableFrom(clazz) &&
						!clazz.getName().equals(IPlayerData.class.getName()) &&
						!clazz.getName().equals(BasePlayerData.class.getName())) {
					// 生成中间子节码
					System.out.println("玩家数据模块：" + clazz.getName());
					Codec simpleTypeCodec = ProtobufProxy.create(clazz);
					codecs.put(clazz.getName(), simpleTypeCodec);
					modules.add((Class<? extends IPlayerData>) clazz);
					Enum<? extends IModule> moduleType = (Enum<? extends IModule>) clazz.getMethod("getModuleType").invoke(clazz.newInstance());
					module.put(moduleType.toString(), clazz.getName());
				}
				if (IData.class.isAssignableFrom(clazz) &&
						!clazz.getName().equals(IData.class.getName())
						&& !Modifier.isAbstract(clazz.getModifiers())
						&& !Modifier.isInterface(clazz.getModifiers())) {
					// 生成中间子节码
					Codec simpleTypeCodec = ProtobufProxy.create(clazz);
					codecs.put(clazz.getName(), simpleTypeCodec);
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 序列化
	 * 将ProtoBuf对象序列化为byte数组
	 *
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] encode(IData data) {
		if (data == null) {
			throw new NullPointerException("空数据");
		}
		String className = null;
		if (data instanceof IPlayerData) {
			className = module.get(((IPlayerData) data).getModuleType().toString());
		}
		if (className == null) {
			className = data.getClass().getName();
		}
		// 序列化
		try {
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			CodedOutputStream outputStream = CodedOutputStream.newInstance(byteArrayOutputStream);
//			codecs.get(className).writeTo(data, outputStream);
//			return byteArrayOutputStream.toByteArray();
			return codecs.get(className).encode(data);
		} catch (IOException e) {
			System.out.println(data.getClass().getName());
			LogService.log(e);
		}
		return new byte[0];
	}

	/**
	 * 反序列化
	 * 将byte数组反序列化为ProtoBuf对象
	 *
	 * @param className 类名 或者 缓存名字
	 * @param data      要被解析的数据
	 * @param <T>       泛型类型
	 * @return 数据
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IData> T decode(String className, byte[] data) {
		try {
			String clazz = module.get(className);
			if (clazz == null) {
				clazz = className;
			}
			Codec codec = codecs.get(clazz);
			if (codec == null) {
				return null;
			}
			return (T) codec.decode(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反序列化
	 * 将String反序列化为ProtoBuf对象
	 *
	 * @param data
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IData> T decode(String className, String data) {
		return decode(className, data.getBytes(Charset.forName("ISO-8859-1")));
	}

	/**
	 * 检测玩家缺少的数据模块并添加该模块
	 *
	 * @param player 玩家
	 */
	public static void checkPlayerData(AbsPlayer player) {
		try {
			List<IPlayerData> init = new ArrayList<>();
			for (Class<? extends IPlayerData> clazz : modules) {
				if (!player.hasModule(clazz)) {
					IPlayerData data = clazz.newInstance();
					player.addPlayerData(data);
					init.add(data);
				}
			}
			for (IPlayerData data : init) {
				data.init(player);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			LogService.log(e);
		}
	}
}