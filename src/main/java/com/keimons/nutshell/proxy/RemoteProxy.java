package com.keimons.nutshell.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 代理
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class RemoteProxy implements IProxy, InvocationHandler {

	/**
	 * 代理类的实例
	 */
	private Object instance;

	/**
	 * 超时时间
	 */
	private long timeout;

	/**
	 * 返回结果
	 */
	private Object result;

	CountDownLatch latch = new CountDownLatch(1);

	public RemoteProxy(Object instance, long timeout) {
		this.instance = instance;
		this.timeout = timeout;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ProxyManager.getInstance().addProxy(this, method, args);
		latch.await(timeout, TimeUnit.MILLISECONDS);
		return result;
	}

	@Override
	public void setResult(Object result) {
		latch.countDown();
		this.result = result;
	}

	@Override
	public Object getInstance() {
		return instance;
	}
}