package com.keimons.platform.network;

/**
 * 异步执行结果监听器
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-01
 * @since 1.8
 **/
public interface IFutureListener<F extends IFuture> {

	/**
	 * 当与{@link IFuture}关联的操作完成时调用
	 *
	 * @param future 异步执行结果 {@link IFuture} 回调
	 */
	void operationComplete(F future) throws Exception;
}