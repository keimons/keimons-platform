package com.keimons.platform.executor;

/**
 * 任务策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface ITaskStrategy {

	Object DEFAULT = new Object();

	/**
	 * 提交一个任务
	 *
	 * @param executor   任务执行策略
	 * @param threadCode 线程码
	 * @param task       任务
	 */
	void commitTask(int executor, int threadCode, Runnable task);
}