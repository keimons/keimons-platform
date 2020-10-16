package com.keimons.platform.executor;

import java.util.Objects;

/**
 * 执行器类型
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ExecutorType {

	/**
	 * 系统中的业务执行器执行策略
	 */
	private static final IExecutorStrategy[] strategies = new IExecutorStrategy[128];

	static {
		strategies[0] = new NoneExecutorPolicy();
	}

	public static IExecutorStrategy getExecutorStrategy(int index) {
		return strategies[index];
	}

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