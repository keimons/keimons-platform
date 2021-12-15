package com.keimons.nutshell.unit;

import com.keimons.nutshell.Nutshell;
import com.keimons.nutshell.Optional;
import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;
import com.keimons.nutshell.module.IGameData;
import com.keimons.nutshell.module.IModule;
import com.keimons.nutshell.module.IModuleSerializable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * 序列化工具
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class SerializeUtil {

	private static final ILogger logger = LoggerFactory.getLogger(SerializeUtil.class);

	/**
	 * 序列化模块
	 *
	 * @param module   模块
	 * @param coercive 是否强制序列化
	 * @param <T>      序列化类型
	 * @return 序列化后的数据 {@code null} 没有数据
	 * @throws IOException 序列化异常
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T serialize(IModule<?> module, boolean coercive) throws IOException {
		var serializable = (IModuleSerializable<T>) Nutshell.get(Optional.SERIALIZE);
		return serializable.serialize(module, coercive);
	}

	/**
	 * 序列化模块
	 *
	 * @param clazz 数据类型
	 * @param <T>   序列化类型
	 * @param <V>   数据化类型
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static <T, V extends IGameData> List<V> deserialize(Class<V> clazz, T data) throws IOException {
		@SuppressWarnings("unchecked")
		IModuleSerializable<T> serialize = (IModuleSerializable<T>) Nutshell.get(Optional.SERIALIZE);
		return serialize.deserialize(clazz, data);
	}
}