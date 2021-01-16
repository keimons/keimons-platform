package com.keimons.platform.modular.committer;

import com.keimons.platform.executor.*;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务提交者策略测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-10-26
 * @since 1.8
 **/
public class CommitterTest {

	public static final int POOL_EXECUTOR_STRATEGY = 1;

	public static final int CODE_EXECUTOR_STRATEGY = 2;

	public static final AtomicInteger NUMBER = new AtomicInteger();

	public static final AtomicInteger CURRENT = new AtomicInteger();

	public static final Lock LOCK = new ReentrantLock();

	static {
		if (ExecutorManager.getExecutorStrategy(POOL_EXECUTOR_STRATEGY) == null) {
			ExecutorManager.registerExecutorStrategy(
					POOL_EXECUTOR_STRATEGY, new PoolExecutorPolicy("Pool", 4)
			);
		}
		if (ExecutorManager.getExecutorStrategy(CODE_EXECUTOR_STRATEGY) == null) {
			ExecutorManager.registerExecutorStrategy(
					CODE_EXECUTOR_STRATEGY, new CodeExecutorPolicy("Code", 4)
			);
		}
	}

	@Test
	public void test() {
		Thread currentThread = Thread.currentThread();
		System.out.println("------------------任务立即提交策略测试------------------");
		for (int i = 0; i < 10; i++) {
			CommitterManager.commitTask(
					CommitterManager.LOCATE_TASK_COMMITTER_POLICY,
					null,
					ExecutorManager.DEFAULT_EXECUTOR_STRATEGY,
					-1,
					() -> {
						assert currentThread == Thread.currentThread();
						System.out.println("执行线程：" + Thread.currentThread());
					}
			);
		}
		System.out.println("------------------任务排队提交策略测试------------------");
		for (int i = 0; i < 100; i++) {
			Thread thread = new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					LOCK.lock();
					int index = NUMBER.getAndIncrement();
					CommitterManager.commitTask(
							CommitterManager.LINKED_TASK_COMMITTER_POLICY,
							ICommitterStrategy.DEFAULT,
							POOL_EXECUTOR_STRATEGY,
							-1,
							() -> {
								int current = CURRENT.getAndIncrement();
								assert index == current;
							}
					);
					LOCK.unlock();
				}
			});
			thread.start();
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
