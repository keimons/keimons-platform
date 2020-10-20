package com.keimons.platform.executor;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 任务管理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class TaskManager {

	public static final int COMMIT_TASK_POLICY = 0;

	public static final int LINKED_TASK_POLICY = 1;

	public static final int DEFAULT_TASK_STRATEGY = LINKED_TASK_POLICY;

	@SuppressWarnings("unchecked")
	private static final Function<? super Object, ? extends ITaskStrategy>[] creators = new Function[16];

	/**
	 * 整个程序中，所有的任务
	 */
	@SuppressWarnings("unchecked")
	private static final Map<Object, ITaskStrategy>[] queues = new ConcurrentHashMap[16];

	static {
		registerTaskStrategy(
				COMMIT_TASK_POLICY,
				null,
				Collections.singletonMap(ITaskStrategy.DEFAULT, new CommitTaskPolicy())
		);
		registerTaskStrategy(
				LINKED_TASK_POLICY,
				LinkedTaskPolicy::new,
				new ConcurrentHashMap<>()
		);
	}

	/**
	 * 提交一个任务
	 *
	 * @param taskStrategy     任务排队类型
	 * @param key              队列唯一
	 * @param executorStrategy 任务执行策略
	 * @param threadCode       线程码
	 * @param work             任务
	 */
	public static void commitTask(int taskStrategy,
								  Object key,
								  int executorStrategy,
								  int threadCode,
								  Runnable work) {
		ITaskStrategy strategy;
		if (taskStrategy == 0) {
			strategy = queues[taskStrategy].get(ITaskStrategy.DEFAULT);
		} else {
			strategy = queues[taskStrategy].computeIfAbsent(key, creators[taskStrategy]);
		}
		strategy.commitTask(executorStrategy, threadCode, work);
	}

	/**
	 * 注册一个任务
	 *
	 * @param taskStrategy 任务排队策略
	 * @param creator      创建者
	 * @param tasks        对应Map
	 */
	public static void registerTaskStrategy(
			int taskStrategy,
			Function<? super Object, ? extends ITaskStrategy> creator,
			Map<Object, ITaskStrategy> tasks) {
		creators[taskStrategy] = creator;
		queues[taskStrategy] = tasks;
	}
}