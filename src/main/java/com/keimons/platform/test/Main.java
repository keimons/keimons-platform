package com.keimons.platform.test;

import com.keimons.platform.fixed.OtherServer;
import com.keimons.platform.thread.IThreadPermission;
import com.keimons.platform.thread.KeimonsClassLoader;
import com.keimons.platform.thread.KeimonsThread;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-06-15
 * @since 1.8
 **/
public class Main {

	public enum Permission implements IThreadPermission {
		ALL, TEST;
	}

	public static void checkPermission(Enum<? extends IThreadPermission> permission) {
		if (!KeimonsThread.hasPermission(permission)) {
			throw new RuntimeException("权限不足");
		}
	}

	public static void main() {
		KeimonsThread.setPermissionClass(Permission.class);
		Thread thread = KeimonsThread.createThread(
				() -> {
					System.out.println(OtherServer.class.getClassLoader());
					checkPermission(Permission.TEST);
					System.out.println(111);
				}, Permission.ALL, Permission.TEST
		);
		System.out.println(App.class.getClassLoader());
		System.out.println(Object.class.getClassLoader());
		thread.setContextClassLoader(new KeimonsClassLoader("KeimonsSystemClassLoader"));
		thread.start();
	}
}