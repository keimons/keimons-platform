package com.keimons.nutshell.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池任务执行策略
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class PoolExecutorPolicy extends BaseExecutorStrategy {

	/**
	 * 执行器
	 */
	private final ThreadPoolExecutor service;

	public PoolExecutorPolicy(String name, int nThreads) {
		super(name, nThreads);
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
	}

	@Override
	public void commit(int threadCode, Runnable task) {
		service.execute(task);
	}

	@Override
	public void submit(int threadCode, Runnable task) {
		Future<?> future = service.submit(task);
		try {
			future.get();
		} catch (Throwable cause) {
			throw new ExecutionFailException(cause.getCause(), "任务执行失败！");
		}
	}

	@Override
	public <T> T submit(int threadCode, Callable<T> task) {
		Future<T> future = service.submit(task);
		try {
			return future.get();
		} catch (Throwable cause) {
			throw new ExecutionFailException(cause.getCause(), "任务执行失败！");
		}
	}

	@Override
	public void shutdown() {
		service.shutdown();
	}
}