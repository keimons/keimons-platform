package com.keimons.nutshell.test;

import com.keimons.nutshell.fixed.OtherServer;
import com.keimons.nutshell.thread.IThreadPermission;
import com.keimons.nutshell.thread.NutshellClassLoader;
import com.keimons.nutshell.thread.NutshellThread;

/**
 * 线程权限
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class Main {

	public enum Permission implements IThreadPermission {
		ALL, TEST;
	}

	public static void checkPermission(Enum<? extends IThreadPermission> permission) {
		if (!NutshellThread.hasPermission(permission)) {
			throw new RuntimeException("权限不足");
		}
	}

	public static void main() {
		NutshellThread.setPermissionClass(Permission.class);
		Thread thread = NutshellThread.createThread(
				() -> {
					System.out.println(OtherServer.class.getClassLoader());
					checkPermission(Permission.TEST);
					System.out.println(111);
				}, Permission.ALL, Permission.TEST
		);
		System.out.println(App.class.getClassLoader());
		System.out.println(Object.class.getClassLoader());
		thread.setContextClassLoader(new NutshellClassLoader("KeimonsSystemClassLoader"));
		thread.start();
	}
}