package com.keimons.platform.thread;

/**
 * 业务处理线程模型
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IExecutorEnum {

	/**
	 * 获取线程命名规则
	 *
	 * @return 线程命名规则
	 */
	String getThreadName();

	/**
	 * 获取线程数量
	 *
	 * @return 线程数量
	 */
	int getThreadNumb();

	/**
	 * 是否按照规则进行路由
	 *
	 * @return 是否路由
	 */
	boolean isRoute();
}