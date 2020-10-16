package com.keimons.platform.executor;

import java.util.concurrent.Callable;

/**
 * 直接业务执行策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class NoneExecutorPolicy extends BaseExecutorStrategy {

	private static final String NAME = "none";

	public NoneExecutorPolicy() {
		this(NAME);
	}

	public NoneExecutorPolicy(String name) {
		super(name, 0);
	}

	@Override
	public void execute(int threadCode, Runnable task) {
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