package com.keimons.platform.module;

/**
 * 组持久化方案，这一步是将可重叠的数据进行合并，合并之后才能存入数据库
 * <p>
 * 系统中提供的一种持久化方案，例如装备，实际上是将数据
 *
 * @param <T> 模块中的数据类型
 * @author monkey1993
 * @version 1.0
 **/
public interface ISingularModule<T extends IGameData & ISingularData> extends IModule<T> {

	/**
	 * 获取数据
	 *
	 * @return 数据
	 */
	T get();

	@Override
	default int size() {
		return 1;
	}
}