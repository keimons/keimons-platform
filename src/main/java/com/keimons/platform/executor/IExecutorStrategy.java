package com.keimons.platform.executor;

import java.util.concurrent.Callable;

/**
 * 任务执行策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IExecutorStrategy {

	/**
	 * 获取策略的名称
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

	/**
	 * 提交一个任务
	 * <p>
	 * 该方法可能会阻塞当前线程，直到任务执行完毕
	 *
	 * @param threadCode 线程码
	 * @param task       任务
	 * @throws ExecutionFailException 任务执行失败异常
	 */
	void submit(int threadCode, Runnable task);

	/**
	 * 提交一个任务
	 *
	 * @param threadCode 线程码
	 * @param task       任务
	 * @param <T>        返回值类型
	 * @return 返回值
	 * @throws ExecutionFailException 任务执行失败异常
	 */
	<T> T submit(int threadCode, Callable<T> task);

	/**
	 * 关闭线程池
	 */
	void shutdown();
}