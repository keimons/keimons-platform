package com.keimons.nutshell.module;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 可重复的数据实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class BaseRepeatedModule<K, T extends IGameData & IRepeatedData<K>> implements IRepeatedModule<K, T> {

	/**
	 * 可重复的数据模块
	 */
	protected ConcurrentHashMap<K, T> repeated = new ConcurrentHashMap<>();

	@Override
	public int size() {
		return repeated.size();
	}

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
	public void upgrade(int before, int current) {
	}

	@Override
	public Collection<T> values() {
		return repeated.values();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		repeated.values().forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return repeated.values().spliterator();
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return repeated.values().iterator();
	}
}