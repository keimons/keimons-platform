package com.keimons.platform.iface;

/**
 * 业务逻辑管理
 * <p>
 * 在这里管理该模块的业务逻辑部分
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
public interface IService extends IEventHandler {

	/**
	 * 模块启动
	 * <p>
	 * 该模块下的数据，通过这里进行启动
	 *
	 * @return 是否启动成功
	 */
	boolean start();

	/**
	 * 模块停止
	 *
	 * @return 是否停止成功
	 */
	boolean shutdown();
}
