package com.keimons.nutshell.network;

/**
 * 连接
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface ISession {

	/**
	 * 获取原始连接
	 *
	 * @param <T> 原始连接类型
	 * @return 原始连接
	 */
	<T> T getSession();


	/**
	 * 写入数据
	 *
	 * @param object 数据
	 * @return 结果回调
	 */
	IFuture write(Object object);
}