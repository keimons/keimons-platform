package com.keimons.platform.proxy;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-05-25
 * @since 1.8
 **/
public class IRpcTestImpl implements IRpcTest {

	@Override
	public String test(String threadName) {
		System.out.println("test 线程=" + Thread.currentThread().getName());
		return threadName + " call " + Thread.currentThread().getName();
	}
}