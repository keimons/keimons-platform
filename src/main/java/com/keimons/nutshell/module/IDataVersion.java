package com.keimons.nutshell.module;

/**
 * 数据版本接口
 * <p>
 * 如果系统中数据版本大于这个版本，那么要对数据版本进行升级
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public interface IDataVersion {

	/**
	 * 获取当前的版本
	 *
	 * @return 当前数据版本
	 */
	int getVersion();
}