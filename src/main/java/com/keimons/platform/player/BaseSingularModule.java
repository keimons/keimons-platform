package com.keimons.platform.player;

import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.module.ISingularModule;

/**
 * 单数据实现
 *
 * @author monkey1993
 * @version 1.0
 **/
public abstract class BaseSingularModule<T extends ISingularPlayerData> implements ISingularModule<T> {

	/**
	 * 玩家数据
	 */
	protected final T singular;

	/**
	 * 构造方法
	 *
	 * @param singular 数据
	 */
	public BaseSingularModule(T singular) {
		this.singular = singular;
	}

	@Override
	public T get() {
		return singular;
	}
}