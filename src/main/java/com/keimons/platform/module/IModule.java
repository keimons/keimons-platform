package com.keimons.platform.module;

import java.util.Collection;

/**
 * 组持久化方案，这一步是将可重叠的数据进行合并，合并之后才能存入数据库
 * <p>
 * 系统中提供的一种持久化方案，例如装备，实际上是将数据
 *
 * @param <T> 模块中的数据类型
 * @author monkey1993
 * @version 1.0
 **/
public interface IModule<T extends IGameData> extends Iterable<T> {

	/**
	 * 是否已改变
	 *
	 * @return 是否已改变
	 */
	default boolean isUpdate() {
		return true;
	}

	/**
	 * 是否压缩
	 *
	 * @return 该数据是否需要压缩
	 */
	default boolean isCompress() {
		return false;
	}

	/**
	 * 获取数据的版本
	 *
	 * @return 数据版本
	 */
	default int getVersion() {
		return 0;
	}

	/**
	 * 获取容器中的元素数量
	 *
	 * @return 元素数量
	 */
	int size();

	/**
	 * 获取模块中所有数据
	 *
	 * @return 模块所有数据
	 */
	Collection<T> values();

	/**
	 * 数据结构升级
	 *
	 * @param before  以前的版本
	 * @param current 当前版本
	 */
	void upgrade(int before, int current);
}