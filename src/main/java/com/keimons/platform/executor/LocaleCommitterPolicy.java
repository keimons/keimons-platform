package com.keimons.platform.executor;

/**
 * 任务立即提交策略
 * <p>
 * 直接将任务提交至任务执行器。
 *
 * @author monkey1993
 * @version 1.0
 * @see ExecutorManager 任务执行器
 * @since 1.8
 **/
public class LocaleCommitterPolicy implements ICommitterStrategy {

	@Override
	public void commit(Object key, int executor, int threadCode, Runnable task) {
		IExecutorStrategy strategy = ExecutorManager.getExecutorStrategy(executor);
		strategy.commit(threadCode, task);
	}

	@Override
	public void refresh() {

	}
}