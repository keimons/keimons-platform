package com.keimons.platform.player;

import com.keimons.platform.iface.IData;

/**
 * 组持久化方案，这一步是将可重叠的数据进行合并，合并之后才能存入数据库
 * <p>
 * 系统中提供的一种持久化方案
 *
 * @param <T>
 * @author monkey1993
 * @version 1.0
 * @date 2020-01-16
 **/
public interface IPersistenceModule<T> extends IData {

	/**
	 * 获取存库数据
	 *
	 * @return 存库数据
	 */
	T getData();
}