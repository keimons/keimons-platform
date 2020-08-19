package com.keimons.platform.thread;

import java.util.Set;

/**
 * 带权限的可执行对象
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsRunnable implements Runnable {

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
	public KeimonsRunnable(Set<? extends Enum<? extends IThreadPermission>> permission, Runnable runnable) {
		this.permission = permission;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		KeimonsThread.setPermission(permission);
		runnable.run();
	}
}