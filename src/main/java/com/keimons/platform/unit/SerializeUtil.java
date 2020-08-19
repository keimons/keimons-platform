package com.keimons.platform.unit;

import com.keimons.platform.iface.IGameData;
import com.keimons.platform.module.IModule;
import com.keimons.platform.module.IModuleSerializable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 序列化工具
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class SerializeUtil {

	/**
	 * 序列化模块
	 *
	 * @param clazz    模块序列化方案
	 * @param module   模块
	 * @param coercive 是否强制序列化
	 * @param <T>      序列化类型
	 * @return 序列化后的数据 {@code null} 没有数据
	 * @throws Exception                 序列化异常
	 * @throws IllegalAccessException    反射创建对象异常
	 * @throws InstantiationException    反射创建对象异常
	 * @throws NoSuchMethodException     反射创建对象异常
	 * @throws InvocationTargetException 反射创建对象异常
	 */
	@Nullable
	public static <T> T serialize(Class<? extends IModuleSerializable<T>> clazz,
								  IModule<?> module, boolean coercive) throws Exception {
		IModuleSerializable<T> serializable = clazz.getDeclaredConstructor().newInstance();
		return serializable.serialize(module, coercive);
	}

	/**
	 * 序列化模块
	 *
	 * @param serializable 序列化方案
	 * @param dataClass    数据类型
	 * @param <T>          序列化类型
	 * @param <V>          数据化类型
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static <T, V extends IGameData> List<V> deserialize(IModuleSerializable<T> serializable,
															   Class<V> dataClass) throws IOException {
		return serializable.deserialize(dataClass);
	}
}