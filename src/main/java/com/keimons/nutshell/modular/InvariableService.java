package com.keimons.nutshell.modular;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 不变的服务器数据
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class InvariableService {

	private static volatile Map<Class<?>, Map<Object, ?>> STATIC_DATA = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> T find(Class<T> clazz, Object key) {
		Map<Object, ?> map = STATIC_DATA.get(clazz);
		if (map == null) {
			return null;
		}
		return (T) map.get(key);
	}

	public static void load() {

	}

	public static void reload() {

	}
}