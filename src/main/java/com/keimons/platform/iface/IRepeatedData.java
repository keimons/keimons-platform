package com.keimons.platform.iface;

/**
 * 可重复的标识
 *
 * @param <T> 数据模块
 * @author monkey1993
 * @version 1.0
 **/
public interface IRepeatedData<T> extends IPlayerData {

	/**
	 * 数据唯一标识
	 * <p>
	 * 如果数据是可重复的，那么，数据需要一个唯一标识，才能真正的找到这个数据
	 *
	 * @return 数据唯一标识
	 */
	T getDataId();
}