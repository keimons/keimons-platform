package com.keimons.nutshell.modular;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 模块管理
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class AssemblyManager {

	/**
	 * 所有模块
	 */
	private static final LinkedHashMap<Class<?>, IAssembly> assemblies = new LinkedHashMap<>();

	/**
	 * 注册一个组件
	 *
	 * @param assembly 组件
	 */
	public static void registerAssembly(Class<?> clazz, IAssembly assembly) {
		assemblies.put(clazz, assembly);
	}

	/**
	 * 获取一个组件
	 *
	 * @param clazz 组件类
	 * @param <T>   组件类型
	 * @return 组件
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IAssembly> T getAssembly(Class<T> clazz) {
		return (T) assemblies.get(clazz);
	}

	/**
	 * 初始化
	 */
	public synchronized static void init() {
		for (Map.Entry<Class<?>, IAssembly> entry : assemblies.entrySet()) {
			entry.getValue().init();
		}
	}

	/**
	 * 启动
	 *
	 * @throws Throwable 启动异常
	 */
	public synchronized static void startup() throws Throwable {
		for (Map.Entry<Class<?>, IAssembly> entry : assemblies.entrySet()) {
			entry.getValue().start();
		}
	}

	/**
	 * 关闭
	 */
	public synchronized static void shutdown() {
		for (Map.Entry<Class<?>, IAssembly> entry : assemblies.entrySet()) {
			entry.getValue().shutdown();
		}
	}
}