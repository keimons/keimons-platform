package com.keimons.platform.executor;

import com.keimons.platform.StrategyAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 任务执行器
 * <p>
 * 系统允许定义最多128个任务执行器。每个执行器需要实现{@link IExecutorStrategy}接口。
 *
 * @author monkey1993
 * @version 1.0
 * @see NoneExecutorPolicy 无操作任务执行策略
 * @see PoolExecutorPolicy 线程池任务执行策略
 * @see CodeExecutorPolicy 线程码任务执行策略
 * @since 1.8
 **/
public class ExecutorManager {

	/**
	 * 框架自带的任务提交策略
	 * <p>
	 * 使用这个策略，会绑定一个唯一的对象，并且，在这个对象中排队提交，当且仅当一个任务完成后，
	 * 下一个任务才会提交到任务执行器中。
	 *
	 * @see NoneExecutorPolicy 排队提交任务策略
	 */
	public static final int NONE_EXECUTOR_STRATEGY = 0;

	/**
	 * 默认的任务提交策略
	 *
	 * @see NoneExecutorPolicy 排队提交任务策略
	 */
	public static final int DEFAULT_EXECUTOR_STRATEGY = NONE_EXECUTOR_STRATEGY;

	/**
	 * 任务执行策略
	 *
	 * @see NoneExecutorPolicy 无操作任务执行策略
	 * @see PoolExecutorPolicy 线程池任务执行策略
	 * @see CodeExecutorPolicy 线程码任务执行策略
	 */
	private static final IExecutorStrategy[] strategies = new IExecutorStrategy[128];

	static {
		// 无操作任务执行器
		registerExecutorStrategy(NONE_EXECUTOR_STRATEGY, new NoneExecutorPolicy());
	}

	/**
	 * 获取一个任务执行策略
	 *
	 * @param executorIndex 任务执行器
	 * @return 任务执行策略
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IExecutorStrategy> T getExecutorStrategy(
			@Range(from = 0, to = 127) int executorIndex) {
		return (T) strategies[executorIndex];
	}

	/**
	 * 使用任务执行策略，执行一个任务
	 *
	 * @param executorIndex 任务执行策略
	 * @param threadCode    线程码
	 * @param task          等待执行的任务
	 */
	public static void executeTask(int executorIndex, int threadCode, Runnable task) {
		strategies[executorIndex].commit(threadCode, task);
	}

	/**
	 * 注册一个任务执行器
	 *
	 * @param executorIndex 任务执行器
	 * @param strategy      任务执行策略
	 */
	public static synchronized void registerExecutorStrategy(
			@Range(from = 0, to = 127) int executorIndex,
			@NotNull IExecutorStrategy strategy) {
		if (Objects.nonNull(strategies[executorIndex])) {
			throw new StrategyAlreadyExistsException("executor", executorIndex);
		}
		strategies[executorIndex] = strategy;
	}
}