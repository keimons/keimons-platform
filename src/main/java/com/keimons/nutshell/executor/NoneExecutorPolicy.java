package com.keimons.nutshell.executor;

import java.util.concurrent.Callable;

/**
 * 无操作任务执行策略，使用调用者线程直接执行任务
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class NoneExecutorPolicy extends BaseExecutorStrategy {

	private static final String NAME = "None";

	public NoneExecutorPolicy() {
		this(NAME);
	}

	public NoneExecutorPolicy(String name) {
		super(name, 0);
	}

	@Override
	public void commit(int threadCode, Runnable task) {
		task.run();
	}

	@Override
	public void submit(int threadCode, Runnable task) {
		task.run();
	}

	@Override
	public <T> T submit(int threadCode, Callable<T> task) {
		try {
			return task.call();
		} catch (Throwable cause) {
			throw new ExecutionFailException(cause.getCause(), "任务执行失败！");
		}
	}

	@Override
	public void shutdown() {

	}
}