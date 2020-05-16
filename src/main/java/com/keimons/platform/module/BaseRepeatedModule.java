package com.keimons.platform.module;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可重复的数据实现
 *
 * @author monkey1993
 * @version 1.0
 **/
public abstract class BaseRepeatedModule<K, T extends IRepeatedPlayerData<K>> implements IRepeatedModule<K, T> {

	/**
	 * 可重复的数据模块
	 */
	protected ConcurrentHashMap<K, T> repeated = new ConcurrentHashMap<>();

	@Override
	public void add(T data) {
		repeated.put(data.getDataId(), data);
	}

	@Override
	public T get(K dataId) {
		return repeated.get(dataId);
	}

	@Override
	public T remove(K dataId) {
		return repeated.remove(dataId);
	}

	@Override
	public Collection<T> toCollection() {
		return repeated.values();
	}

	@Override
	public void upgrade(int before, int current) {
	}
}