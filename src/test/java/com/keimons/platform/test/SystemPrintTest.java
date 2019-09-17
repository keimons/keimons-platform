package com.keimons.platform.test;

import com.keimons.platform.log.LogService;

/**
 * System.out和System.err重定向测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
public class SystemPrintTest {

	public static void main(String[] args) {
		LogService.redirectSystemPrint();
		System.out.println(111);
		System.err.println(222);
	}
}