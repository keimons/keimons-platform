package com.keimons.nutshell.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class LocateProxy implements InvocationHandler {

	/**
	 * 代理类的实例
	 */
	private Object instance;

	public LocateProxy(Object instance) {
		this.instance = instance;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(instance, args);
	}
}