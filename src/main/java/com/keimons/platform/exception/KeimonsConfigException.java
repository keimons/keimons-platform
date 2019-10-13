package com.keimons.platform.exception;

/**
 * 配置错误异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsConfigException extends RuntimeException {

	/**
	 * 配置错误异常
	 *
	 * @param properties 配置项
	 */
	public KeimonsConfigException(String properties) {
		super("错误的配置项：" + properties);
	}
}