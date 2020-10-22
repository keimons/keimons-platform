package com.keimons.platform.executor;

/**
 * 任务提交策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface ICommitterStrategy {

	Object DEFAULT = new Object();

	/**
	 * 提交一个任务
	 *
	 * @param key              提交者的唯一表示
	 * @param executorStrategy 任务执行策略
	 * @param threadCode       线程码
	 * @param task             任务
	 */
	void commit(Object key, int executorStrategy, int threadCode, Runnable task);

	/**
	 * 获取刷新时间
	 */
	void refresh();
}