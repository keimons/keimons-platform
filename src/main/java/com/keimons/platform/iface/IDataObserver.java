package com.keimons.platform.iface;

/**
 * 数据观察者
 * <p>
 * 用于监控数据是否改变
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-10-02
 * @since 1.8
 */
public interface IDataObserver {

	/**
	 * 获取最新数据
	 *
	 * @param notnull 是否强制获取数据
	 * @return 最新数据{@code null}则表示无最新数据
	 */
	byte[] latest(boolean notnull);
}