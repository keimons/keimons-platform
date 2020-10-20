package com.keimons.platform.executor;

import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 任务执行器
 * <p>
 * 系统允许定义最多128个任务执行器。每个执行器需要实现{@link IExecutorStrategy}接口。
 *
 * @author monkey1993
 * @version 1.0
 * @see NoneExecutorPolicy 无操作业务执行策略
 * @see PoolExecutorPolicy 线程池业务执行策略
 * @see CodeExecutorPolicy 线程码业务执行策略
 * @since 1.8
 **/
public class ExecutorManager {

	/**
	 * 系统中的业务执行策略
	 */
	private static final IExecutorStrategy[] strategies = new IExecutorStrategy[128];

	static {
		// 无操作业务执行器
		strategies[0] = new NoneExecutorPolicy();
	}

	/**
	 * 获取一个任务执行器
	 *
	 * @param executor 任务执行器
	 * @return 任务执行器
	 */
	public static IExecutorStrategy getExecutorStrategy(@Range(from = 0, to = 127) int executor) {
		return strategies[executor];
	}

	/**
	 * 注册一个任务执行器
	 *
	 * @param executor 任务执行器
	 * @param strategy 任务执行策略
	 */
	public static synchronized void registerExecutorStrategy(@Range(from = 0, to = 127) int executor, IExecutorStrategy strategy) {
		if (Objects.nonNull(strategies[executor])) {
			throw new RuntimeException("already exists strategy ");
		}
		strategies[executor] = strategy;
	}
}