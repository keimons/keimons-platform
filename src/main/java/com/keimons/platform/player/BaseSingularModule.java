package com.keimons.platform.player;

import com.keimons.platform.iface.ISingularData;

import java.util.Collection;
import java.util.Collections;

/**
 * 单数据实现
 *
 * @author monkey1993
 * @version 1.0
 **/
public abstract class BaseSingularModule<T extends ISingularData> implements IModule<T> {

	/**
	 * 玩家数据
	 */
	protected T singular;

	@Override
	public void addPlayerData(T singularData) {
		this.singular = singularData;
	}

	@Override
	public T getPlayerData(Object dataId) {
		return singular;
	}

	@Override
	public Collection<T> getPlayerData() {
		return Collections.singletonList(singular);
	}
}