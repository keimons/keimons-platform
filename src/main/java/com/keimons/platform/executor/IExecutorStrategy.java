package com.keimons.platform.executor;

import java.util.concurrent.Callable;

/**
 * 业务执行器策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IExecutorStrategy {

	/**
	 * 获取线程池的名称
	 *
	 * @return 线程池名称
	 */
	String getName();

	/**
	 * 获取策略线程池大小
	 *
	 * @return 线程池大小
	 */
	int size();

	/**
	 * 执行任务
	 * <p>
	 * 依赖于不同的实现，该任务可能在新线程池或线程中执行，也可能直接使用调用者的线程立即执行。
	 *
	 * @param threadCode 线程码
	 * @param task       任务
	 */
	void execute(int threadCode, Runnable task);

	void submit(int threadCode, Runnable task);

	<T> T submit(int threadCode, Callable<T> task);

	/**
	 * 线程是否结束
	 */
	void shutdown();
}