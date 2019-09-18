package com.keimons.platform.test;

import com.keimons.platform.console.ConsoleService;

/**
 * System.out和System.err重定向测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
public class ConsoleServiceTest {

	public static void main(String[] args) {
		ConsoleService.init();
		System.out.println(111);
		System.err.println(222);
	}
}