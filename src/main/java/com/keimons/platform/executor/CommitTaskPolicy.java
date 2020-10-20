package com.keimons.platform.executor;

/**
 * 直接提交任务队列策略
 * <p>
 * 直接将任务提交至任务执行器。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class CommitTaskPolicy implements ITaskStrategy {

	@Override
	public void commitTask(int executor, int threadCode, Runnable task) {
		IExecutorStrategy strategy = ExecutorManager.getExecutorStrategy(executor);
		strategy.commit(threadCode, task);
	}
}