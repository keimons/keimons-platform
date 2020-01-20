package com.keimons.platform.unit;

import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.module.IModuleSerializable;
import com.keimons.platform.player.IModule;

import java.io.IOException;
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
	 * @param module   模块
	 * @param coercive 是否强制序列化
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static <T> T serialize(
			Class<? extends IModuleSerializable<T>> clazz,
			IModule<? extends IPlayerData> module,
			boolean coercive) throws IOException, IllegalAccessException, InstantiationException {
		IModuleSerializable<T> serializable = clazz.newInstance();
		return serializable.serialize(module, coercive);
	}

	/**
	 * 序列化模块
	 *
	 * @param serializable 序列化方案
	 * @param dataClass    数据类型
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static <T> List<? extends IPlayerData> deserialize(
			IModuleSerializable<T> serializable,
			Class<? extends IPlayerData> dataClass) throws IOException {
		return serializable.deserialize(dataClass);
	}
}