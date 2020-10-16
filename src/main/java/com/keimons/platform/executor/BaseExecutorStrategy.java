package com.keimons.platform.executor;

/**
 * 任务执行策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BaseExecutorStrategy implements IExecutorStrategy {

	/**
	 * 策略名称
	 */
	protected final String name;

	/**
	 * 线程数量
	 */
	protected final int nThreads;

	public BaseExecutorStrategy(String name, int nThreads) {
		this.name = name;
		this.nThreads = nThreads;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int size() {
		return nThreads;
	}
}