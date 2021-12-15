package com.keimons.nutshell.network;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Mina写入数据的异步执行结果实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class MinaWriteFuture extends BaseFuture<IoFutureListener<WriteFuture>> implements IWriteFuture {

	/**
	 * 系统连接
	 */
	private final ISession session;

	/**
	 * 原始异步执行结果
	 */
	private final WriteFuture future;

	/**
	 * 构造器
	 *
	 * @param session 系统连接
	 * @param future  原始异步执行结果
	 */
	public MinaWriteFuture(ISession session, WriteFuture future) {
		this.session = session;
		this.future = future;
	}

	@Override
	public ISession getSession() {
		return session;
	}

	@Override
	@SuppressWarnings("unchecked")
	public WriteFuture getFuture() {
		return future;
	}

	@Override
	public MinaWriteFuture addListener(final IFutureListener<? extends IFuture> listener) {
		IoFutureListener<WriteFuture> trueListener = addListener0(listener);
		if (trueListener != null) {
			future.addListener(trueListener);
		}
		return this;
	}

	@Override
	public MinaWriteFuture removeListener(IFutureListener<? extends IFuture> listener) {
		IoFutureListener<WriteFuture> trueListener = removeListener0(listener);
		if (trueListener != null) {
			future.removeListener(trueListener);
		}
		return this;
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected IoFutureListener<WriteFuture> createListener(IFutureListener listener) {
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
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return future.isDone();
	}

	@Override
	public Void get() throws InterruptedException, ExecutionException {
		future.await();
		return null;
	}

	@Override
	public Void get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		future.await(timeout, unit);
		return null;
	}
}