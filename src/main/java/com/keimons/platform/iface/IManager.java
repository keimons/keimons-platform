package com.keimons.platform.iface;

/**
 * 静态数据管理
 */
public interface IManager {

	/**
	 * 初始化 <br \>
	 * 警告：在初始化过程中，禁止加载任何数据 <br \>
	 * 所有数据必须通过reloadData统一执行 <br \>
	 * 相互关联数据，按照加载顺序进行加载 <br \>
	 */
	default void init() {
	}

	/**
	 * 重新加载数据 <br \>
	 * 警告：加在静态数据过程中，必须锁定对象
	 */
	default void reload() {
		this.init();
	}

	/**
	 * 系统框架允许在每天0点处理一些全服数据
	 * <p>
	 * 跨天0点数据处理
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
	 * 关闭 <br \>
	 * 警告：关闭服务器时，必须先讲内存中的所有数据保存至数据库
	 *
	 * @return 是否可以关闭
	 */
	default boolean shutdown() {
		return true;
	}
}