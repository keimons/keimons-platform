package com.keimons.nutshell.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 代理类
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProxyManager {

	private Map<Method, Method> methods = new HashMap<>();

	private static final ProxyManager instance = new ProxyManager();

	private LinkedBlockingQueue<Runnable> task = new LinkedBlockingQueue<>();

	private ProxyManager() {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					Runnable runnable = task.take();
					runnable.run();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "KeimonsProxy");
		thread.start();
	}

	public static ProxyManager getInstance() {
		return instance;
	}

	public void addMethod(Method method, Method instance) {
		this.methods.put(method, instance);
	}

	public void addProxy(RemoteProxy proxy, Method method, Object[] args) {
		task.add(() -> {
			try {
				proxy.setResult(this.methods.get(method).invoke(proxy.getInstance(), args));
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}
}