package com.keimons.platform.thread;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-06-15
 * @since 1.8
 **/
public class KeimonsThread {

	/**
	 * 权限权限
	 */
	private static final ThreadLocal<Set<? extends Enum<? extends IThreadPermission>>> LOCAL_PERMISSION = new ThreadLocal<>();

	/**
	 * 线程自增ID
	 */
	private static final AtomicInteger index = new AtomicInteger();

	/**
	 * 线程权限枚举
	 */
	private static Class<? extends Enum<? extends IThreadPermission>> clazz;

	/**
	 * 创建一个线程
	 *
	 * @return 线程
	 */
	@SuppressWarnings("unchecked")
	public static Thread createThread() {
		return createThread(null);
	}

	/**
	 * 创建一个线程
	 *
	 * @param runnable    执行对象
	 * @param permissions 线程权限
	 * @return 线程
	 */
	@SuppressWarnings("unchecked")
	public static Thread createThread(Runnable runnable, Enum<? extends IThreadPermission>... permissions) {
		return createThread(
				runnable, "KeimonsThread-" + index.getAndIncrement(), permissions
		);
	}

	/**
	 * 创建一个线程
	 *
	 * @param runnable    执行对象
	 * @param name        线程名字
	 * @param permissions 线程权限
	 * @return 线程
	 */
	@SuppressWarnings("unchecked")
	public static Thread createThread(Runnable runnable, @NotNull String name, Enum<? extends IThreadPermission>... permissions) {
		return createThread(null, runnable, name, permissions);
	}

	/**
	 * 创建一个线程
	 *
	 * @param group       线程组
	 * @param runnable    执行对象
	 * @param name        线程名字
	 * @param permissions 执行权限
	 * @return 线程
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> Thread
	createThread(ThreadGroup group, Runnable runnable, @NotNull String name,
				 Enum<? extends IThreadPermission>... permissions) {
		if (runnable == null) {
			return new Thread(group, null, name);
		} else {
			Set<Enum<? extends IThreadPermission>> set;
			if (clazz == null) {
				set = new HashSet<>();
			} else {
				Class<E> clazz = (Class<E>) KeimonsThread.clazz;
				set = (Set<Enum<? extends IThreadPermission>>) EnumSet.noneOf(clazz);
			}
			set.addAll(Arrays.asList(permissions));
			KeimonsRunnable keimons = new KeimonsRunnable(set, runnable);
			return new Thread(group, keimons, name);
		}
	}

	/**
	 * 设置权限枚举类
	 * <p>
	 * 如果设置了权限枚举类，则会创建{@link EnumSet}否则创建{@link HashSet}，
	 * 前者对于枚举拥有更高的效率，后者可以使用多个枚举类。
	 *
	 * @param clazz 权限枚举类
	 */
	public static void setPermissionClass(Class<? extends Enum<? extends IThreadPermission>> clazz) {
		KeimonsThread.clazz = clazz;
	}

	/**
	 * 设置线程权限
	 *
	 * @param permissions 线程权限
	 */
	public static void setPermission(Set<? extends Enum<? extends IThreadPermission>> permissions) {
		LOCAL_PERMISSION.set(permissions);
	}

	/**
	 * 检查这个线程是否拥有这个权限
	 *
	 * @param permissions 权限
	 * @return 是否拥有此权限
	 */
	public static boolean hasPermission(Enum<? extends IThreadPermission> permissions) {
		Set<? extends Enum<? extends IThreadPermission>> enums = LOCAL_PERMISSION.get();
		return enums == null || enums.contains(permissions);
	}
}