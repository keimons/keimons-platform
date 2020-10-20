package com.keimons.platform.executor;

/**
 * 任务提交策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface ICommitterStrategy {

	/**
	 * 提交一个任务
	 *
	 * @param executor   任务执行策略
	 * @param threadCode 线程码
	 * @param task       任务
	 */
	void commitTask(int executor, int threadCode, Runnable task);

	/**
	 * 获取刷新时间
	 *
	 * @return 上次刷新时间
	 */
	long getRefreshTime();
}