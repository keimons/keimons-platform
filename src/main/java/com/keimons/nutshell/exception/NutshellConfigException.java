package com.keimons.nutshell.exception;

/**
 * 配置错误异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class NutshellConfigException extends RuntimeException {

	/**
	 * 配置错误异常
	 *
	 * @param properties 配置项
	 */
	public NutshellConfigException(String properties) {
		super("错误的配置项：" + properties);
	}
}