package com.keimons.platform.module;

import com.keimons.platform.iface.IGameData;
import com.keimons.platform.iface.ISerializable;

import java.io.IOException;
import java.util.List;

/**
 * 针对于模块的序列化方案
 * <p>
 * 将玩家的模块数据序列化为指定的数据类型。
 *
 * @param <T> 存储数据类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IModuleSerializable<T> extends ISerializable {

	/**
	 * 序列化为存库数据
	 * <p>
	 * 获取合并后的数据，这是存入数据库的最终数据。
	 *
	 * @param module   模块
	 * @param coercive 强制存储
	 * @return 序列化后的数据
	 * @throws IOException 序列化错误
	 */
	T serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException;

	/**
	 * 反序列化为玩家数据
	 *
	 * @param clazz 数据类型
	 * @return 玩家数据
	 * @throws IOException 反序列化异常
	 */
	<V extends IGameData> List<V> deserialize(Class<V> clazz) throws IOException;

	/**
	 * 针对于模块的序列化方案
	 * <p>
	 * 将玩家的模块数据序列化为指定的数据类型。
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 **/
	interface ISerializeModule<T> extends ISerializable {

		/**
		 * 获取模块中所有的数据
		 *
		 * @return 数据
		 */
		List<T> getElements();

		/**
		 * 是否压缩
		 *
		 * @return 该数据是否经过压缩
		 */
		boolean isCompress();

		/**
		 * 获取序列化后的数据。
		 * <p>
		 * 获取合并后的数据，这是存入数据库的最终数据。
		 *
		 * @param module   模块
		 * @param coercive 强制存储
		 * @throws IOException 序列化错误
		 */
		void serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException;
	}
}