package com.keimons.platform.modular;

/**
 * 组件系统
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IAssembly {

	/**
	 * 数据初始化
	 * <p>
	 * 包括配置、全局数据。
	 */
	void init();

	/**
	 * 启动
	 *
	 * @throws Throwable 启动失败情况下抛出异常 由系统捕获异常，并且放弃本次启动。
	 */
	void start() throws Throwable;

	/**
	 * 关闭
	 */
	void shutdown();
}