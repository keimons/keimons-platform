package com.keimons.platform.iface;

/**
 * 可重复的数据结构
 *
 * @param <T> 数据唯一标识符类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IRepeatedGameData<T> extends IGameData {

	/**
	 * 数据唯一标识
	 * <p>
	 * 如果数据是可重复的，那么，数据需要一个唯一标识，才能真正的找到这个数据
	 *
	 * @return 数据唯一标识
	 */
	T getDataId();
}