package com.keimons.platform.module;

/**
 * 可重复的游戏数据结构
 *
 * @param <K> 数据主键类型
 * @author monkey1993
 * @version 1.0
 * @see com.keimons.platform.module.IRepeatedModule 重复数据模块
 * @since 1.8
 **/
public interface IRepeatedData<K> {

	/**
	 * 数据主键
	 * <p>
	 * 如果数据是可重复的，那么，数据需要一个唯一主键，才能真正的找到这个数据。
	 *
	 * @return 数据唯一标识
	 */
	K getDataId();
}