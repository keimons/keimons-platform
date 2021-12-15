package com.keimons.nutshell.exception;

/**
 * 模块安装异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ModuleException extends RuntimeException {

	/**
	 * 模块安装异常
	 *
	 * @param message 异常信息
	 */
	public ModuleException(String message) {
		super(message);
	}
}