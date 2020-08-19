package com.keimons.platform.thread;

/**
 * 权限检测函数
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
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