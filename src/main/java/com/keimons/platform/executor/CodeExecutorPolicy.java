package com.keimons.platform.executor;

import com.keimons.platform.log.LogService;

import java.util.concurrent.*;

/**
 * 线程码任务执行策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class CodeExecutorPolicy extends BaseExecutorStrategy {

	/**
	 * 任务执行器
	 */
	private final Executor[] executors;

	/**
	 * 线程池
	 */
	private final ThreadPoolExecutor service;

	public CodeExecutorPolicy(String name, int nThreads) {
		super(name, nThreads);

		executors = new Executor[nThreads];
		service = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
		for (int i = 0; i < nThreads; i++) {
			executors[i] = new Executor();
			service.execute(executors[i]);
		}
	}

	@Override
	public void commit(int threadCode, Runnable task) {
		executors[threadCode % nThreads].execute(task);
	}

	@Override
	public void submit(int threadCode, Runnable task) {
		executors[threadCode % nThreads].submit(task);
	}

	@Override
	public <T> T submit(int threadCode, Callable<T> task) {
		return executors[threadCode % nThreads].submit(task);
	}

	@Override
	public void shutdown() {
		for (Executor executor : executors) {
			executor.shutdown();
		}
		service.shutdownNow();
	}

	/**
	 * 消息执行队列
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 **/
	static class Executor implements Runnable {

		/**
		 * 线程安全的阻塞队列
		 */
		private final BlockingDeque<Runnable> queue;

		/**
		 * 执行器
		 * <p>
		 * 消息的真正执行者
		 */
		public Executor() {
			queue = new LinkedBlockingDeque<>();
		}

		/**
		 * 是否执行中
		 */
		private volatile boolean running = true;

		@Override
		public void run() {
			while (running) {
				try {
					Runnable runnable = queue.take();
					runnable.run();
				} catch (Throwable e) {
					LogService.error(e);
				}
			}
		}

		/**
		 * 增加一个任务
		 *
		 * @param task 队尾
		 */
		public void execute(Runnable task) {
			queue.offer(task);
		}

		/**
		 * 增加一个任务
		 *
		 * @param task 队尾
		 */
		public void submit(Runnable task) {
			RunnableFuture<Void> future = new FutureTask<>(task, null);
			queue.offer(future);
			try {
				future.get();
			} catch (Throwable cause) {
				throw new ExecutionFailException(cause.getCause(), "任务执行失败！");
			}
		}

		/**
		 * 增加一个任务
		 *
		 * @param task 队尾
		 * @return 是否成功
		 */
		public <T> T submit(Callable<T> task) {
			FutureTask<T> future = new FutureTask<>(task);
			queue.offer(future);
			try {
				return future.get();
			} catch (Throwable cause) {
				throw new ExecutionFailException(cause.getCause(), "任务执行失败！");
			}
		}

		/**
		 * 关闭线程
		 */
		public void shutdown() {
			running = false;
			// 增加一个空消息，保证能够正常结束任务
			queue.add(() -> {
			});
		}
	}
}