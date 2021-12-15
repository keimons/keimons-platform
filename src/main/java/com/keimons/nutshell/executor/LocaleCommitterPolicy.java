package com.keimons.nutshell.executor;

/**
 * 任务立即提交策略
 * <p>
 * 直接将任务提交至任务执行器。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @see ExecutorManager 任务执行器
 * @since 11
 **/
public class LocaleCommitterPolicy implements ICommitterStrategy {

	@Override
	public void commit(Object key, int executorStrategy, int threadCode, Runnable task) {
		IExecutorStrategy strategy = ExecutorManager.getExecutorStrategy(executorStrategy);
		strategy.commit(threadCode, task);
	}

	@Override
	public void refresh() {

	}
}