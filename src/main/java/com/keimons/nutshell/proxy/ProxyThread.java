package com.keimons.nutshell.proxy;

import java.util.function.Supplier;

/**
 * 代理工具
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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