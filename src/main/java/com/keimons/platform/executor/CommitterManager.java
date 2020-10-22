package com.keimons.platform.executor;

import com.keimons.platform.StrategyAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 任务提交管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class CommitterManager {

	/**
	 * 框架自带的任务提交策略
	 * <p>
	 * 使用这个策略，将会立即提交任务到任务执行器中。
	 *
	 * @see LocaleCommitterPolicy 立即提交任务策略
	 */
	public static final int LOCATE_TASK_COMMITTER_POLICY = 0;

	/**
	 * 框架自带的任务提交策略
	 * <p>
	 * 使用这个策略，会绑定一个唯一的对象，并且，在这个对象中排队提交，当且仅当一个任务完成后，
	 * 下一个任务才会提交到任务执行器中。
	 *
	 * @see LinkedCommitterPolicy 排队提交任务策略
	 */
	public static final int LINKED_TASK_COMMITTER_POLICY = 1;

	/**
	 * 默认的任务提交策略
	 *
	 * @see LocaleCommitterPolicy 立即提交任务策略
	 * @see LinkedCommitterPolicy 排队提交任务策略
	 */
	public static final int DEFAULT_COMMITTER_STRATEGY = LINKED_TASK_COMMITTER_POLICY;

	/**
	 * 提交策略
	 */
	private static final ICommitterStrategy[] strategies = new ICommitterStrategy[127];

	static {
		registerCommitterStrategy(LOCATE_TASK_COMMITTER_POLICY, new LocaleCommitterPolicy());
		registerCommitterStrategy(LINKED_TASK_COMMITTER_POLICY, new LinkedCommitterPolicy());

		/**
		 * 启动一个守护线程，定时检测整个程序中过期的Key
		 */
		Thread thread = new Thread(() -> {
			while (true) {
				for (ICommitterStrategy strategy : strategies) {
					if (strategy != null) {
						strategy.refresh();
					}
				}
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "CommitterRefresh");
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 获取一个任务提交策略
	 *
	 * @param committerIndex 任务提交者
	 * @return 任务提交策略
	 */
	public static ICommitterStrategy getCommitterStrategy(
			@Range(from = 0, to = 127) int committerIndex) {
		return strategies[committerIndex];
	}

	/**
	 * 使用任务提交策略，提交一个任务
	 *
	 * @param committerIndex   任务提交策略
	 * @param key              提交者标识
	 * @param executorStrategy 任务执行策略
	 * @param threadCode       线程码
	 * @param task             等待执行的任务
	 */
	public static void commitTask(int committerIndex,
								  Object key,
								  int executorStrategy,
								  int threadCode,
								  Runnable task) {
		strategies[committerIndex].commit(key, executorStrategy, threadCode, task);
	}

	/**
	 * 注册一个任务提交策略
	 *
	 * @param committerIndex 任务提交者
	 * @param strategy       任务提交策略
	 */
	public synchronized static void registerCommitterStrategy(
			@Range(from = 0, to = 127) int committerIndex,
			@NotNull ICommitterStrategy strategy) {
		if (Objects.nonNull(strategies[committerIndex])) {
			throw new StrategyAlreadyExistsException("committer", committerIndex);
		}
		strategies[committerIndex] = strategy;
	}
}