package com.keimons.platform.unit;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.keimons.platform.iface.ISerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * POJO转化为byte[]工具
 */
public class JProtobufUtil {

	static {
		// 由程序自行缓存，关闭工具的自带缓存
		ProtobufProxy.enableCache(false);
	}

	/**
	 * 整个jar包中所有的ProtoBuf对象
	 */
	private static Map<Class<? extends ISerializable>, Codec<ISerializable>> codecs = new HashMap<>();

	/**
	 * 序列化
	 * 将ProtoBuf对象序列化为byte数组
	 *
	 * @param data 数据
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static byte[] encode(ISerializable data) throws IOException {
		data.encode();
		return codecs.get(data.getClass()).encode(data);
	}

	/**
	 * 反序列化
	 * 将byte数组反序列化为Java对象
	 *
	 * @param clazz 要被解析的类
	 * @param data  要被解析的数据
	 * @param <T>   泛型类型
	 * @return Java对象
	 * @throws IOException 反序列化异常
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ISerializable> T decode(Class<T> clazz, byte[] data) throws IOException {
		Codec<T> codec = (Codec<T>) codecs.get(clazz);
		if (codec == null) {
			synchronized (JProtobufUtil.class) {
				codec = (Codec<T>) codecs.get(clazz);
				if (codec == null) {
					codec = ProtobufProxy.create(clazz, true);
					codecs.put(clazz, (Codec<ISerializable>) codec);
				}
			}
		}
		T decode = codec.decode(data);
		if (decode != null) {
			decode.decode();
		}
		return decode;
	}
}