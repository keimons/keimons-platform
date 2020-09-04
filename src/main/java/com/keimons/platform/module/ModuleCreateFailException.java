package com.keimons.platform.module;

/**
 * 模块创建失败异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ModuleCreateFailException extends RuntimeException {

	/**
	 * 模块创建失败异常
	 *
	 * @param clazz 要创建模块的类
	 * @param e     异常信息
	 */
	public ModuleCreateFailException(Class<?> clazz, Throwable e) {
		super("reflection create " + clazz.getName() + " failed", e);
	}
}