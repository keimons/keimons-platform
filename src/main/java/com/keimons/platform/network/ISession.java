package com.keimons.platform.network;

/**
 * 连接
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-29
 * @since 1.8
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