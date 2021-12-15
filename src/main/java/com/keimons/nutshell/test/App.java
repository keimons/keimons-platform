package com.keimons.nutshell.test;

import com.keimons.nutshell.thread.NutshellClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试类
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class App {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		ClassLoader loader = new NutshellClassLoader("KeimonsSystemClassLoader");
		Class<?> clazz0 = Class.forName("com.keimons.nutshell.test.Main", true, loader);
		Method method0 = clazz0.getMethod("main");
		method0.invoke(null);
		Class<?> clazz1 = loader.loadClass("com.keimons.nutshell.test.Main");
		Method method1 = clazz1.getMethod("main");
		method1.invoke(null);
	}
}