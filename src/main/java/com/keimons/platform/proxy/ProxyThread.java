package com.keimons.platform.proxy;

import java.util.function.Supplier;

/**
 * 代理工具
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProxyThread {

	public static <T> T run(Supplier<T> supplier, boolean sync) {
		return supplier.get();
	}

	public static <T> T run(Supplier<T> supplier) {
		return supplier.get();
	}

	public static void run(Runnable runnable, boolean sync) {
		runnable.run();
	}

	public static void run(Runnable runnable) {
		runnable.run();
	}

	public void test() {
		run(() -> {
		});
		String string = run(() -> null);
	}
}