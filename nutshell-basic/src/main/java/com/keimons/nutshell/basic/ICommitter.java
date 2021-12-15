package com.keimons.nutshell.basic;

/**
 * 提交策略
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public interface ICommitter {

	/**
	 * 提交任务
	 *
	 * @param executorStrategy 任务执行策略
	 * @param threadCode       任务线程码
	 * @param task             任务
	 */
	void commit(int executorStrategy, int threadCode, Runnable task);
}