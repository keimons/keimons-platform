package com.keimons.platform.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理工具
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsProxy {

	static Map<Class<?>, Object> proxies = new HashMap<>();

	public static void addProxy(Class<?> clazz, Object instance) {
		proxies.put(clazz, instance);
	}

	/**
	 * 异步代理
	 *
	 * @param clazz 被代理的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> T asyncProxy(Class<T> clazz) {
		InvocationHandler handler = new RemoteProxy(proxies.get(clazz), 1000L);
		return (T) Proxy.newProxyInstance(KeimonsProxy.class.getClassLoader(), new Class[]{clazz}, handler);
	}

	/**
	 * 同步代理
	 *
	 * @param clazz 被代理的类
	 */
	public static <T> T syncProxy(Class<T> clazz) {
		return null;
	}

	public static void main(String[] args) throws NoSuchMethodException {
		System.out.println("当前线程名："+Thread.currentThread().getName());
		proxies.put(IRpcTest.class, new IRpcTestImpl());
		Method faceMethod = IRpcTest.class.getMethod("test", String.class);
		Method implMethod = IRpcTestImpl.class.getMethod("test", String.class);
		ProxyManager.getInstance().addMethod(faceMethod, implMethod);

		IRpcTest proxy = asyncProxy(IRpcTest.class);
		System.out.println(proxy.test(Thread.currentThread().getName()));
	}
}