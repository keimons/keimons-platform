package com.keimons.platform.executor;

/**
 * 业务
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Task {

	/**
	 * 线程码
	 */
	private int threadCode;

	/**
	 * 业务
	 */
	private Runnable runnable;

	public Task(Runnable runnable) {
		this.runnable = runnable;
	}

	public Task(int threadCode, Runnable runnable) {
		this.threadCode = threadCode;
		this.runnable = runnable;
	}

	public int getThreadCode() {
		return threadCode;
	}

	public void setThreadCode(int threadCode) {
		this.threadCode = threadCode;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
}