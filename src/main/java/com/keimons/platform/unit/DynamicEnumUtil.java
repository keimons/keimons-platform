package com.keimons.platform.unit;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-02
 * @since 1.8
 **/
public class DynamicEnumUtil {

	public static <T extends Enum<?>> void addEnum(Class<T> enumType, String name) throws Throwable {
		addEnum(enumType, name, new Class[0], new Object[0]);
	}

	public static <T extends Enum<?>> void addEnum(Class<T> enumType, String name, Class<?>[] types, Object[] params) throws Throwable {
		Class<?> clazz = Array.newInstance(enumType, 0).getClass();
		MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(enumType, MethodHandles.lookup());
		MethodHandle getter = lookup.findStaticGetter(enumType, "$VALUES", clazz);
		@SuppressWarnings("unchecked")
		T[] oldValues = (T[]) getter.invoke();
		try {
			// 3. build new enum
			List<Class<?>> typeList = new ArrayList<>();
			typeList.add(String.class);
			typeList.add(int.class);
			typeList.addAll(Arrays.asList(types));
			MethodType mt = MethodType.methodType(void.class, typeList.toArray(new Class[0]));

			List<Object> paramsList = new ArrayList<>();
			paramsList.add(name);
			paramsList.add(oldValues.length);
			paramsList.addAll(Arrays.asList(params));

			MethodHandle constructor = lookup.findConstructor(enumType, mt);
			@SuppressWarnings("unchecked")
			T newValue = (T) constructor.invokeWithArguments(paramsList);

			// 4. add new value
			List<T> newValues = new ArrayList<>(Arrays.asList(oldValues));
			newValues.add(newValue);

			// 5. Set new values field
			Field valuesField = enumType.getDeclaredField("$VALUES");

			Unsafe unsafe = UnsafeUtil.getUnsafe();
			long address = unsafe.staticFieldOffset(valuesField);
			@SuppressWarnings("unchecked")
			T[] empty = (T[]) Array.newInstance(enumType, 0);
			unsafe.putObject(enumType, address, newValues.toArray(empty));

			// 清空缓存 防止Enum.valueOf("");报错
			Field cache = Class.class.getDeclaredField("enumConstants");
			address = unsafe.objectFieldOffset(cache);
			unsafe.getAndSetObject(enumType, address, null);

			cache = Class.class.getDeclaredField("enumConstantDirectory");
			address = unsafe.objectFieldOffset(cache);
			unsafe.getAndSetObject(enumType, address, null);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}