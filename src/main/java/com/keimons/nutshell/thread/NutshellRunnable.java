package com.keimons.nutshell.thread;

import java.util.Set;

/**
 * 带权限的可执行对象
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class NutshellRunnable implements Runnable {

	/**
	 * 线程权限
	 */
	private final Set<? extends Enum<? extends IThreadPermission>> permission;

	/**
	 * 真实的执行对象
	 */
	private final Runnable runnable;

	/**
	 * 带有权限的任务
	 *
	 * @param permission 权限
	 * @param runnable   执行对象
	 */
	public NutshellRunnable(Set<? extends Enum<? extends IThreadPermission>> permission, Runnable runnable) {
		this.permission = permission;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		NutshellThread.setPermission(permission);
		runnable.run();
	}
}