package com.keimons.platform;

import com.keimons.platform.iface.IManager;
import com.keimons.platform.iface.IService;
import com.keimons.platform.network.MessageConverter;

import java.util.Collection;

/**
 * 初始化
 * <p>
 * 启动优先级
 * 1.管理器
 * 2.服务
 */
public class KeimonsServer {

	/**
	 * 系统版本
	 */
	public volatile static int VERSION = 0;

	/**
	 * 系统平台
	 */
	private static KeimonsPlatform<?> platform;

	public static KeimonsConfig KeimonsConfig;

	/**
	 * 启动入口
	 * <p>
	 * 采用默认的配置
	 *
	 * @param converter 消息转化器
	 * @param <T>       输入/输出类型
	 */
	public static <T> void start(MessageConverter<T> converter) {
		start(com.keimons.platform.KeimonsConfig.defaultConfig(), converter);
	}

	/**
	 * 启动入口
	 * <p>
	 * 采用默认的配置文件
	 *
	 * @param path      文件路径
	 * @param converter 消息转化器
	 * @param <T>       输入/输出类型
	 * @deprecated 暂未完成
	 */
	@Deprecated
	public static <T> void start(String path, MessageConverter<T> converter) {

	}

	/**
	 * 启动入口
	 *
	 * @param keimonsConfig 启动入口
	 * @param converter     消息转化器
	 * @param <T>           输入/输出类型
	 */
	public static <T> void start(KeimonsConfig keimonsConfig, MessageConverter<T> converter) {
		KeimonsConfig = keimonsConfig;
		platform = new KeimonsPlatform<>(keimonsConfig, converter);
		platform.start();
	}

	/**
	 * 重新加载静态数据
	 */
	public static void reload() {
		platform.reload();
	}

	/**
	 * 关闭入口
	 */
	public static void shutdown() {
		platform.shutdown();
	}

	public static KeimonsPlatform<?> getPlatform() {
		return platform;
	}

	/**
	 * 获取管理
	 *
	 * @param clazz 管理
	 * @param <T>   返回值类型
	 * @return 管理
	 */
	@SuppressWarnings("unchecked")
	public <T extends IManager> T getManager(Class<T> clazz) {
		return (T) platform.managers.get(clazz);
	}

	/**
	 * 获取所有管理
	 *
	 * @return 所有管理
	 */
	public Collection<IManager> getManagers() {
		return platform.managers.values();
	}

	/**
	 * 获取服务
	 *
	 * @param clazz 服务
	 * @param <T>   返回值类型
	 * @return 服务
	 */
	@SuppressWarnings("unchecked")
	public <T extends IService> T getService(Class<T> clazz) {
		return (T) platform.services.get(clazz);
	}

	/**
	 * 获取所有服务
	 *
	 * @return 所有服务
	 */
	public Collection<IService> getServices() {
		return platform.services.values();
	}
}