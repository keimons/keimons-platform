package com.keimons.platform.executor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务排队提交策略
 * <p>
 * 将任务排队提交到任务执行器中，当且仅当当前的任务完成了，才会将下一个任务提交到任务执行器中。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class LinkedCommitterPolicy implements ICommitterStrategy {

	/**
	 * 空闲中的状态
	 */
	private static final boolean BUSY = true;

	/**
	 * 运行中的状态
	 */
	private static final boolean FREE = false;

	/**
	 * 任务队列的key
	 */
	private final Object key;

	/**
	 * 是否正在执行中
	 */
	private final AtomicBoolean busy = new AtomicBoolean(FREE);

	/**
	 * 等待执行的任务
	 */
	private final ConcurrentLinkedQueue<Work> works = new ConcurrentLinkedQueue<>();

	/**
	 * 上次刷新时间
	 */
	private long refreshTime;

	public LinkedCommitterPolicy(Object key) {
		this.key = key;
	}

	@Override
	public void commitTask(int executor, int threadCode, Runnable task) {
		Work work = new Work(executor, threadCode, task);
		works.offer(work);
		refreshTime = System.currentTimeMillis();
		tryStartTask();
	}

	/**
	 * 尝试开始一个任务
	 */
	private void tryStartTask() {
		if (!works.isEmpty() && busy.compareAndSet(FREE, BUSY)) {
			Work work = works.peek();
			if (work == null) {
				busy.set(FREE);
				return;
			}
			IExecutorStrategy strategy = ExecutorManager.getExecutorStrategy(work.getExecutor());
			strategy.commit(work.getThreadCode(), buildLinkedTask(work));
		}
	}

	/**
	 * 完成任务
	 *
	 * @param finish 已经完成的任务
	 */
	public void finishTask(Work finish) {
		works.poll();
		busy.set(FREE);
	}

	private Runnable buildLinkedTask(Work work) {
		return () -> {
			try {
				work.getTask().run();
			} finally {
				finishTask(work);
			}
			tryStartTask();
		};
	}

	public Object getKey() {
		return key;
	}

	public ConcurrentLinkedQueue<Work> getTasks() {
		return works;
	}

	@Override
	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	/**
	 * 等待执行的任务
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 **/
	public static class Work {

		/**
		 * 任务执行策略
		 */
		private int executor;

		/**
		 * 线程码
		 */
		private int threadCode;

		/**
		 * 准备执行的任务
		 */
		private Runnable task;

		public Work(int executor, int threadCode, Runnable task) {
			this.executor = executor;
			this.threadCode = threadCode;
			this.task = task;
		}

		public int getExecutor() {
			return executor;
		}

		public void setExecutor(int executor) {
			this.executor = executor;
		}

		public int getThreadCode() {
			return threadCode;
		}

		public void setThreadCode(int threadCode) {
			this.threadCode = threadCode;
		}

		public Runnable getTask() {
			return task;
		}

		public void setTask(Runnable task) {
			this.task = task;
		}
	}
}