package com.keimons.platform.thread;

public enum DefaultExecutorConfig implements IExecutorConfig {
	AUTO(false, "AUTO", 0, false),
	FAST(true, "EXECUTOR-FAST-", 20, true),
	SLOW(true, "EXECUTOR-SLOW-", 20, true),
	RULE(true, "EXECUTOR-RULE-", 20, false),
	LEAGUE(true, "LoginThread", 1, true),
	;

	/**
	 * 是否活跃的
	 */
	private boolean active;

	/**
	 * 线程命名规则
	 */
	private String threadName;

	/**
	 * 线程数量
	 */
	private int threadNumb;

	/**
	 * 线程执行器类型
	 */
	private boolean route;

	/**
	 * 默认的执行器
	 *
	 * @param threadName 执行器名字
	 * @param threadNumb 执行器线程数量
	 * @param route      执行器类型
	 */
	DefaultExecutorConfig(boolean active, String threadName, int threadNumb, boolean route) {
		this.active = active;
		this.threadName = threadName;
		this.threadNumb = threadNumb;
		this.route = route;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public String getThreadName() {
		return threadName;
	}

	@Override
	public int getThreadNumb() {
		return threadNumb;
	}

	@Override
	public boolean isRoute() {
		return route;
	}
}