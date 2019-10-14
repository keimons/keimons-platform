package com.keimons.platform.process;

import com.keimons.platform.log.LogService;

import java.util.concurrent.*;

/**
 * 消息执行
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProcessorQueue implements Runnable {

	private final BlockingDeque<Runnable> queue;

	public ProcessorQueue() {
		queue = new LinkedBlockingDeque<>();
	}

	private boolean run = true;

	@Override
	public void run() {
		while (run) {
			try {
				Runnable reqs = queue.take();
				reqs.run();
			} catch (Throwable e) {
				LogService.error(e);
			}
		}
	}

	/**
	 * 增加一个任务
	 *
	 * @param runnable 队尾
	 * @return 是否成功
	 */
	public void add(Runnable runnable) {
		queue.add(runnable);
	}

	/**
	 * 增加一个任务
	 *
	 * @param callable 队尾
	 * @return 是否成功
	 */
	public <T> T offer(Callable<T> callable) throws ExecutionException, InterruptedException {
		FutureTask<T> task = new FutureTask<>(callable);
		queue.offerFirst(task);
		return task.get();
	}
}