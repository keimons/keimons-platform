package com.keimons.nutshell.network;

import java.util.concurrent.Future;

/**
 * 异步执行的结果
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IFuture extends Future<Void> {

	/**
	 * 获取连接
	 *
	 * @return 连接
	 */
	ISession getSession();

	/**
	 * 获取原始异步执行的结果
	 *
	 * @param <T> 原始异步执行结果类型
	 * @return 原始异步执行的结果
	 */
	<T> T getFuture();

	/**
	 * 增加监听器
	 *
	 * @param listener 监听器
	 * @return 异步执行结果
	 */
	IFuture addListener(IFutureListener<? extends IFuture> listener);

	/**
	 * 移除监听器
	 *
	 * @param listener 监听器
	 * @return 异步执行结果
	 */
	IFuture removeListener(IFutureListener<? extends IFuture> listener);
}