package com.keimons.platform.iface;

/**
 * 服务
 */
public interface IService {

	/**
	 * 服务器启动
	 *
	 * @return 是否开服成功
	 */
	boolean startup();

	/**
	 * 系统框架允许在每天0点处理一些全服数据
	 * <p>
	 * 跨天0数据处理
	 */
	default void otherDay0() {
	}

	/**
	 * 系统框架允许在每天5点处理一些全服数据
	 * <p>
	 * 跨天5数据处理
	 */
	default void otherDay5() {
	}

	/**
	 * 服务停止
	 *
	 * @return 是否关服成功
	 */
	boolean shutdown();
}
