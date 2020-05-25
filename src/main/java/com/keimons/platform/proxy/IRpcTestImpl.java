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
		return threadName + " call " + Thread.currentThread().getName();
	}
}
