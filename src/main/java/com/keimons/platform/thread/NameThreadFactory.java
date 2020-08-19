package com.keimons.platform.thread;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 按照名称并且带有线程优先级的线程工厂
 * <p>
 * 相比较于系统的线程工厂，增加了可以自定义线程优先级的功能。
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-05-09
 * @since 1.8
 **/
public class NameThreadFactory implements ThreadFactory {

	/**
	 * 线程组
	 */
	private final ThreadGroup group;

	/**
	 * 线程ID
	 */
	private final AtomicInteger number = new AtomicInteger(1);

	/**
	 * 线程优先级
	 */
	private final int priority;

	/**
	 * 线程名称前缀
	 */
	private final String prefix;

	/**
	 * 线程权限
	 */
	private final Enum<? extends IThreadPermission>[] permissions;

	/**
	 * 创建线程池工厂
	 * <p>
	 * 使用{@link Thread#NORM_PRIORITY}默认的线程优先级
	 *
	 * @param prefix      线程名前缀
	 * @param permissions 线程权限
	 */
	public NameThreadFactory(@NotNull String prefix, Enum<? extends IThreadPermission>... permissions) {
		this(Thread.NORM_PRIORITY, prefix, permissions);
	}

	/**
	 * 创建线程池工厂
	 *
	 * @param priority    线程优先级
	 * @param prefix      线程名前缀
	 * @param permissions 线程权限
	 */
	public NameThreadFactory(@Range(from = 1, to = 10) int priority,
							 @NotNull String prefix,
							 Enum<? extends IThreadPermission>... permissions) {
		this.permissions = permissions;
		this.priority = priority;
		this.prefix = prefix;
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
	}

	@Override
	public Thread newThread(@NotNull Runnable task) {
		String name = prefix + number.getAndIncrement();
		Thread thread = KeimonsThread.createThread(group, task, name, permissions);
		if (thread.isDaemon()) {
			thread.setDaemon(false);
		}
		thread.setPriority(priority);
		return thread;
	}
}