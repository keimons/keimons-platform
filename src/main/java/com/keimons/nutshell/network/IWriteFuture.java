package com.keimons.nutshell.network;

/**
 * 写入的异步执行结果
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IWriteFuture extends IFuture {

	@Override
	IWriteFuture addListener(IFutureListener<? extends IFuture> listener);

	@Override
	IWriteFuture removeListener(IFutureListener<? extends IFuture> listener);
}