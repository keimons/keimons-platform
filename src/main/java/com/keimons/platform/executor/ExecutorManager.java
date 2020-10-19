package com.keimons.platform.executor;

import java.util.Objects;

/**
 * 业务执行器
 * <p>
 * 系统允许定义最多128个业务执行器。每个执行器需要实现{@link IExecutorStrategy}接口。
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
	 * 系统中的业务执行器执行策略
	 */
	private static final IExecutorStrategy[] strategies = new IExecutorStrategy[128];

	static {
		// 无操作业务执行器
		strategies[0] = new NoneExecutorPolicy();
	}

	/**
	 * 获取一个任务执行器
	 *
	 * @param index 任务执行器index
	 * @return 任务执行器
	 */
	public static IExecutorStrategy getExecutorStrategy(int index) {
		return strategies[index];
	}

	/**
	 * 注册一个业务执行器
	 *
	 * @param index    业务执行器index
	 * @param strategy 业务执行策略
	 */
	public static synchronized void registerExecutorStrategy(int index, IExecutorStrategy strategy) {
		if (index < 0 || strategies.length <= index) {
			throw new ArrayIndexOutOfBoundsException("index " + index + " out of range [0, 127]");
		}
		if (Objects.nonNull(strategies[index])) {
			throw new RuntimeException("already exists strategy ");
		}
		strategies[index] = strategy;
	}
}