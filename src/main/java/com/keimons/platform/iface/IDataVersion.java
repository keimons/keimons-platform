package com.keimons.platform.iface;

/**
 * 数据版本接口
 * <p>
 * 如果系统中数据版本大于这个版本，那么要对数据版本进行升级
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IDataVersion {

	/**
	 * 获取当前的版本
	 *
	 * @return 当前数据版本
	 */
	int getVersion();
}