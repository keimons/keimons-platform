package com.keimons.platform.network;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Netty写入数据的异步执行结果实现
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-01
 * @since 1.8
 **/
public class NettyWriteFuture
		extends BaseFuture<GenericFutureListener<? extends Future<? super Void>>>
		implements IWriteFuture {

	/**
	 * 系统连接
	 */
	private final ISession session;

	/**
	 * 原始异步执行结果
	 */
	private final ChannelFuture future;

	/**
	 * 构造方法
	 *
	 * @param session 系统连接
	 * @param future  原始异步执行结果
	 */
	public NettyWriteFuture(ISession session, ChannelFuture future) {
		this.session = session;
		this.future = future;
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ChannelFuture getFuture() {
		return future;
	}

	@Override
	public NettyWriteFuture addListener(IFutureListener<? extends IFuture> listener) {
		GenericFutureListener<? extends Future<? super Void>> lsr = addListener0(listener);
		if (lsr != null) {
			future.addListener(lsr);
		}
		return this;
	}

	@Override
	public NettyWriteFuture removeListener(IFutureListener<? extends IFuture> listener) {
		GenericFutureListener<? extends Future<? super Void>> lsr = removeListener0(listener);
		if (lsr != null) {
			future.removeListener(lsr);
		}
		return this;
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected GenericFutureListener<? extends Future<? super Void>>
	createListener(IFutureListener listener) {
		return (future) -> {
			try {
				listener.operationComplete(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public Void get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	@Override
	public Void get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return future.get(timeout, unit);
	}
}