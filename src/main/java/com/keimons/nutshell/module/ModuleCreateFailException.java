package com.keimons.nutshell.module;

/**
 * 模块创建失败异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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