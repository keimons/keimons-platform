package com.keimons.nutshell.network;

/**
 * 异步执行结果监听器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IFutureListener<F extends IFuture> {

	/**
	 * 当与{@link IFuture}关联的操作完成时调用
	 *
	 * @param future 异步执行结果 {@link IFuture} 回调
	 */
	void operationComplete(F future) throws Exception;
}