package com.keimons.platform.test;

import com.keimons.platform.iface.IEventCode;

/**
 * 测试事件枚举
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 */
public enum TestEventCodeEnum implements IEventCode {

	/**
	 * 登录
	 */
	LOGIN,

	/**
	 * 登出
	 */
	LOGOUT
}