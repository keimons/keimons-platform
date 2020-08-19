package com.keimons.platform.test;

import com.keimons.platform.thread.KeimonsClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-06-16
 * @since 1.8
 **/
public class App {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		ClassLoader loader = new KeimonsClassLoader("KeimonsSystemClassLoader");
		Class<?> clazz0 = Class.forName("com.keimons.platform.test.Main", true, loader);
		Method method0 = clazz0.getMethod("main");
		method0.invoke(null);
		Class<?> clazz1 = loader.loadClass("com.keimons.platform.test.Main");
		Method method1 = clazz1.getMethod("main");
		method1.invoke(null);
	}
}