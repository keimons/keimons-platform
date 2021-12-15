package com.keimons.nutshell.thread;

/**
 * 权限检测函数
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
@FunctionalInterface
public interface PermissionChecker {

	/**
	 * 权限检测函数
	 *
	 * @param locale 已有权限
	 * @return 是否满足
	 */
	boolean check(IThreadPermission locale);
}