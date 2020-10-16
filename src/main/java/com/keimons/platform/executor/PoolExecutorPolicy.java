package com.keimons.platform.executor;

import java.util.concurrent.*;

/**
 * 线程池业务执行策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class PoolExecutorPolicy extends BaseExecutorStrategy {

	/**
	 * 执行器
	 */
	private final ExecutorService service;

	public PoolExecutorPolicy(String name, int nThreads) {
		super(name, nThreads);
		service = Executors.newFixedThreadPool(nThreads);
	}

	@Override
	public void execute(int threadCode, Runnable task) {
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