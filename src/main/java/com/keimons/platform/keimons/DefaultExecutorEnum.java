package com.keimons.platform.keimons;

import com.keimons.platform.executor.IExecutorEnum;

public enum DefaultExecutorEnum implements IExecutorEnum {
	/**
	 * 快速消息执行器，例如消息执行时间小于10毫秒。
	 */
	FAST("EXECUTOR-FAST-", 20, false),

	/**
	 * 慢速消息执行器，例如消息执行时间大于10毫秒。
	 */
	SLOW("EXECUTOR-SLOW-", 20, false),

	/**
	 * 可选线程消息执行器，根据消息体，选择消息执行器。
	 */
	RULE("EXECUTOR-RULE-", 20, true);

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
	DefaultExecutorEnum(String threadName, int threadNumb, boolean route) {
		this.threadName = threadName;
		this.threadNumb = threadNumb;
		this.route = route;
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