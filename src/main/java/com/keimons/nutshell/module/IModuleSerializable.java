package com.keimons.nutshell.module;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * 针对于模块的序列化方案
 * <p>
 * 将玩家的模块数据序列化为指定的数据类型。
 *
 * @param <T> 存储数据类型
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IModuleSerializable<T> {

	/**
	 * 序列化为存库数据
	 * <p>
	 * 获取合并后的数据，这是存入数据库的最终数据。
	 *
	 * @param module   模块
	 * @param coercive 强制存储
	 * @return 序列化后的数据 {@code null} 表示没有数据需要存储
	 * @throws IOException 序列化错误
	 */
	@Nullable
	T serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException;

	/**
	 * 反序列化为玩家数据
	 *
	 * @param clazz 数据类型
	 * @param <V>   数据类型
	 * @return 玩家数据
	 * @throws IOException 反序列化异常
	 */
	<V extends IGameData> List<V> deserialize(Class<V> clazz, T data) throws IOException;

	/**
	 * 持久化容器
	 */
	interface ISerializeEntity extends ISerializable {

	}

	/**
	 * 针对于模块的序列化方案
	 * <p>
	 * 将玩家的模块数据序列化为指定的数据类型。
	 *
	 * @author houyn[monkey@keimons.com]
	 * @version 1.0
	 * @since 11
	 **/
	interface ISerializeModule<T> extends ISerializable {

		/**
		 * 获取模块中所有的数据
		 *
		 * @return 数据
		 */
		List<T> getElements();
	}
}