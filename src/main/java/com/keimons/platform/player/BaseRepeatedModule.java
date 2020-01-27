package com.keimons.platform.player;

import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.module.IRepeatedModule;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可重复的数据实现
 *
 * @author monkey1993
 * @version 1.0
 **/
public abstract class BaseRepeatedModule<T extends IRepeatedPlayerData> implements IRepeatedModule<T> {

	protected ConcurrentHashMap<Object, T> repeated = new ConcurrentHashMap<>();

	@Override
	public void add(T data) {
		repeated.put(data.getDataId(), data);
	}

	@Override
	public T get(Object dataId) {
		return repeated.get(dataId);
	}

	@Override
	public T remove(Object dataId) {
		return repeated.remove(dataId);
	}

	@Override
	public Collection<T> toCollection() {
		return repeated.values();
	}
}