package com.keimons.platform.exception;

/**
 * @author monkey1993
 * @version 1.0
 * @since 1.8
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