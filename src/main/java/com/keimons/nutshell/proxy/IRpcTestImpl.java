package com.keimons.nutshell.proxy;

/**
 * 测试实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class IRpcTestImpl implements IRpcTest {

	@Override
	public String test(String threadName) {
		System.out.println("test 线程=" + Thread.currentThread().getName());
		return threadName + " call " + Thread.currentThread().getName();
	}
}