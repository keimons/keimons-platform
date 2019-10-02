package com.keimons.platform.unit;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.keimons.platform.iface.IData;
import com.keimons.platform.log.LogService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * POJO转化为byte[]工具
 */
public class CodeUtil {

	/**
	 * 整个jar包中所有的ProtoBuf对象
	 */
	private static Map<Class<? extends IData>, Codec<IData>> codecs = new HashMap<>();

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
				synchronized (CodeUtil.class) {
					codec = (Codec<T>) codecs.get(clazz);
					if (codec == null) {
						// 由程序自行缓存，关闭工具的自带缓存
						ProtobufProxy.enableCache(false);
						codec = ProtobufProxy.create(clazz, true);
						codecs.put(clazz, (Codec<IData>) codec);
					}
				}
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
}