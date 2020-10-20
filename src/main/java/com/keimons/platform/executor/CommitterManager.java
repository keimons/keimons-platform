package com.keimons.platform.executor;

import com.keimons.platform.StrategyAlreadyExistsException;
import org.jetbrains.annotations.Range;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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

	@SuppressWarnings("unchecked")
	private static final Function<? super Object, ? extends ICommitterStrategy>[] creators = new Function[16];

	/**
	 * 整个程序中，所有的任务
	 */
	@SuppressWarnings("unchecked")
	private static final Map<Object, ICommitterStrategy>[] strategies = new ConcurrentHashMap[16];

	static {
		Map<Object, ICommitterStrategy> simple = Collections.singletonMap(
				LocaleCommitterPolicy.DEFAULT, new LocaleCommitterPolicy()
		);
		registerCommitterStrategy(
				LOCATE_TASK_COMMITTER_POLICY,
				null,
				simple
		);
		registerCommitterStrategy(
				LINKED_TASK_COMMITTER_POLICY,
				LinkedCommitterPolicy::new,
				new ConcurrentHashMap<>()
		);
	}

	/**
	 * 使用任务提交策略，提交一个任务
	 *
	 * @param committerStrategy 任务排队类型
	 * @param key               任务队列标识
	 * @param executorStrategy  任务执行策略
	 * @param threadCode        线程码
	 * @param task              等待执行的任务
	 */
	public static void commitTask(int committerStrategy,
								  Object key,
								  int executorStrategy,
								  int threadCode,
								  Runnable task) {
		ICommitterStrategy strategy;
		if (committerStrategy == LOCATE_TASK_COMMITTER_POLICY) {
			strategy = strategies[committerStrategy].get(LocaleCommitterPolicy.DEFAULT);
		} else {
			strategy = strategies[committerStrategy].computeIfAbsent(key, creators[committerStrategy]);
		}
		strategy.commitTask(executorStrategy, threadCode, task);
	}

	/**
	 * 注册一个任务提交策略
	 *
	 * @param committerStrategy 任务排队策略
	 * @param creator           任务队列创建
	 * @param tasks             对应Map
	 */
	public synchronized static void registerCommitterStrategy(
			@Range(from = 0, to = 15) int committerStrategy,
			Function<? super Object, ? extends ICommitterStrategy> creator,
			Map<Object, ICommitterStrategy> tasks) {
		if (Objects.nonNull(strategies[committerStrategy])) {
			throw new StrategyAlreadyExistsException("committer", committerStrategy);
		}
		if (Objects.isNull(tasks)) {
			throw new NullPointerException();
		}
		creators[committerStrategy] = creator;
		strategies[committerStrategy] = tasks;
	}
}