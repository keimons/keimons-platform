package com.keimons.platform.module;

/**
 * 组持久化方案，这一步是将可重叠的数据进行合并，合并之后才能存入数据库
 * <p>
 * 系统中提供的一种持久化方案，例如装备，实际上是将数据
 *
 * @param <K> 模块中的数据Key类型
 * @param <T> 模块中的数据类型
 * @author monkey1993
 * @version 1.0
 **/
public interface IRepeatedModule<K, T extends IRepeatedPlayerData<?>> extends IModule<T> {

	/**
	 * 增加数据
	 * <p>
	 * 讲一个模块的所有数据合并起来，最终转化为一个整体，存入数据库。需要注意的是，一旦一个模块
	 * 中有一个数据需要存储，那么，整个模块都应该是被存储的。
	 *
	 * @param data 玩家数据
	 */
	void add(T data);

	/**
	 * 获取数据
	 *
	 * @param dataId 数据ID
	 * @return 数据
	 */
	T get(K dataId);

	/**
	 * 移除数据
	 *
	 * @param dataId 数据ID
	 * @return 数据
	 */
	T remove(K dataId);
}