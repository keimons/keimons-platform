package com.keimons.platform.iface;

/**
 * 静态数据管理
 * <p>
 * 一般都会在这里存放静态的表数据，静态数据声明周期中总共会发生4个事件
 * 1.load   初始化
 * 2.reload 重加载
 * 3.unload 卸载
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
public interface IManager {

	/**
	 * 加载
	 * <p>
	 * 将数据从txt、json、xml、db中加载到内存数据
	 */
	default void load() {
	}

	/**
	 * 重加载
	 * <p>
	 * 重新加载数据，用于表数据的热更新
	 */
	default void reload() {
		this.load();
	}

	/**
	 * 卸载
	 * <p>
	 * 数据卸载，用于模块的数据卸载
	 */
	default void unload() {
	}
}