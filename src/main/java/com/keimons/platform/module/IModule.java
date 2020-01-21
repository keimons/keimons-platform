package com.keimons.platform.module;

import com.keimons.platform.iface.ISerializable;
import com.keimons.platform.iface.IPlayerData;

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
public interface IModule<T extends IPlayerData> extends ISerializable {

	/**
	 * 获取模块中所有数据
	 *
	 * @return 模块所有数据
	 */
	Collection<T> toCollection();
}