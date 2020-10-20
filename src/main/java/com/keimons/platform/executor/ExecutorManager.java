package com.keimons.platform.executor;

import com.keimons.platform.StrategyAlreadyExistsException;
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
		strategies[NONE_EXECUTOR_STRATEGY] = new NoneExecutorPolicy();
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
	 * @param executorStrategy 任务执行器
	 * @param strategy         任务执行策略
	 */
	public static synchronized void registerExecutorStrategy(
			@Range(from = 0, to = 127) int executorStrategy, IExecutorStrategy strategy) {
		if (Objects.nonNull(strategies[executorStrategy])) {
			throw new StrategyAlreadyExistsException("executor", executorStrategy);
		}
		strategies[executorStrategy] = strategy;
	}
}