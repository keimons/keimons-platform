package com.keimons.platform.network;

/**
 * 写入的异步执行结果
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-01
 * @since 1.8
 **/
public interface IWriteFuture extends IFuture {

	@Override
	IWriteFuture addListener(IFutureListener<? extends IFuture> listener);

	@Override
	IWriteFuture removeListener(IFutureListener<? extends IFuture> listener);
}