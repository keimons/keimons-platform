package com.keimons.platform.modular;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-11-12
 * @since 1.8
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