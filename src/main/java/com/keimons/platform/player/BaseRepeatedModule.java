package com.keimons.platform.player;

import com.keimons.platform.iface.IRepeatedData;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可重复的数据实现
 *
 * @author monkey1993
 * @version 1.0
 **/
public abstract class BaseRepeatedModule<T extends IRepeatedData> implements IModule<T> {

	protected ConcurrentHashMap<Object, T> repeated = new ConcurrentHashMap<>();

	@Override
	public void addPlayerData(T repeatedData) {
		repeated.put(repeatedData.getDataId(), repeatedData);
	}

	@Override
	public T getPlayerData(Object dataId) {
		return repeated.get(dataId);
	}

	@Override
	public Collection<T> getPlayerData() {
		return repeated.values();
	}
}