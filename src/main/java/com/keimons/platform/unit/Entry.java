package com.keimons.platform.unit;

import java.util.Map;

/**
 * Key-Value结构，存储单个的键值
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 */
public class Entry<K, V> implements Map.Entry<K, V> {

	/**
	 * 键 键是不允许被改变的
	 */
	private final K key;

	/**
	 * 值 该键对应的值
	 */
	private V value;

	Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public final K getKey() {
		return key;
	}

	public final V getValue() {
		return value;
	}

	public final V setValue(V newValue) {
		V oldValue = value;
		value = newValue;
		return oldValue;
	}
}