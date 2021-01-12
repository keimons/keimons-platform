package keimons.basic;

/**
 * 提交策略
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