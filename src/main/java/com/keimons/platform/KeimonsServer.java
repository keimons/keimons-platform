package com.keimons.platform;

import com.keimons.platform.annotation.AManager;
import com.keimons.platform.annotation.AModule;
import com.keimons.platform.annotation.AService;
import com.keimons.platform.console.ConsoleService;
import com.keimons.platform.event.EventService;
import com.keimons.platform.game.GameDataManager;
import com.keimons.platform.iface.IManager;
import com.keimons.platform.iface.IService;
import com.keimons.platform.log.LogService;
import com.keimons.platform.player.PlayerDataManager;
import com.keimons.platform.process.ProcessorManager;
import com.keimons.platform.process.KeimonsExecutor;
import com.keimons.platform.quartz.SchedulerService;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.TimeUtil;

import java.util.*;

/**
 * 初始化
 * <p>
 * 启动优先级
 * 1.管理器
 * 2.服务
 */
public class KeimonsServer {

	/**
	 * 系统配置文件
	 */
	public static KeimonsConfig KeimonsConfig;

	/**
	 * 系统版本
	 */
	public volatile static int VERSION = 0;

	/**
	 * 所有的管理器
	 */
	private static Map<Class<?>, IManager> managers = new HashMap<>();

	/**
	 * 所有的服务器
	 */
	private static Map<Class<?>, IService> services = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends IManager> T getManager(Class<T> clazz) {
		return (T) managers.get(clazz);
	}

	/**
	 * 获取所有管理
	 *
	 * @return 所有管理
	 */
	public static Collection<IManager> getManagers() {
		return managers.values();
	}

	@SuppressWarnings("unchecked")
	public static <T extends IService> T getService(Class<T> clazz) {
		return (T) services.get(clazz);
	}

	/**
	 * 获取所有服务
	 *
	 * @return 所有服务
	 */
	public static Collection<IService> getServices() {
		return services.values();
	}

	/**
	 * 启动入口
	 * <p>
	 * 采用默认的配置
	 */
	public static void start() {
		start(com.keimons.platform.KeimonsConfig.defaultConfig());
	}

	/**
	 * 启动入口
	 * <p>
	 * 采用默认的配置文件
	 *
	 * @param path 文件路径
	 * @deprecated 暂未完成
	 */
	@Deprecated
	public static void start(String path) {

	}

	/**
	 * 启动入口
	 *
	 * @param keimonsConfig 启动入口
	 */
	public static void start(KeimonsConfig keimonsConfig) {
		KeimonsConfig = keimonsConfig;
		if (keimonsConfig.isConsoleRedirect()) {
			ConsoleService.init();
			System.out.println("启用控制台输出重定向！");
		} else {
			System.out.println("禁用控制台输出重定向！");
		}
		LogService.init();
		SchedulerService.init();
		EventService.init();
		List<Package> packages = new ArrayList<>();
		for (Package pkg : Package.getPackages()) {
			AModule module = pkg.getAnnotation(AModule.class);
			if (module != null) {
				packages.add(pkg);
			}
		}
		packages.sort((Comparator.comparingInt(pkg -> pkg.getAnnotation(AModule.class).Priority())));
		for (Package pkg : packages) {
			System.out.println("************************* 开始安装模块 *************************");
			AModule module = pkg.getAnnotation(AModule.class);
			System.out.println("模块安装：" + module.Name() + "，安装顺序：" + module.Priority());
			initModule(pkg.getName());
			System.out.println("************************* 完成安装模块 *************************");
		}
		KeimonsExecutor.init();
		KeimonsTcpNet.init();
	}

	/**
	 * 初始化所有模块
	 *
	 * @param packageName 包名
	 */
	private static void initModule(String packageName) {
		// Logger、Processor、Job、PlayerData、GameData
		KeimonsServer.addManager(packageName);
		KeimonsServer.addService(packageName);

		ProcessorManager.addProcessor(packageName);
		SchedulerService.addJobs(packageName);
		PlayerDataManager.addPlayerData(packageName);
		GameDataManager.addGameData(packageName);
	}

	/**
	 * 关闭入口
	 */
	public static void shutdown() {
		// 关闭Netty
		System.out.println("服务器准备关闭！");
		KeimonsTcpNet.close();

		try {
			// 暂停15秒以便Netty处理完剩余逻辑
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long time = TimeUtil.currentTimeMillis();
		for (IService service : services.values()) {
			service.shutdown();
		}
		System.out.println("服务器关闭耗时：" + (TimeUtil.currentTimeMillis() - time));
	}

	/**
	 * 重新加载静态数据
	 */
	public static void reload() {
		for (IManager manager : managers.values()) {
			manager.reload();
		}
	}

	/**
	 * 初始化管理
	 */
	private static void addManager(String packageName) {
		List<Class<IManager>> list = ClassUtil.load(packageName, AManager.class);
		for (Class<IManager> clazz : list) {
			System.out.println("正在安装模块管理器：" + clazz.getSimpleName());
			try {
				IManager manager = clazz.newInstance();
				managers.put(manager.getClass(), manager);
				manager.load();
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e, clazz.getSimpleName() + "安装模块管理器失败");
			}
			System.out.println("成功安装模块管理器：" + clazz.getSimpleName());
		}
	}

	/**
	 * 初始化服务
	 */
	private static void addService(String packageName) {
		List<Class<IService>> list = ClassUtil.load(packageName, AService.class);
		for (Class<IService> clazz : list) {
			System.out.println("正在安装模块服务器：" + clazz.getSimpleName());
			try {
				IService service = clazz.newInstance();
				services.put(service.getClass(), service);
				EventService.registerEvent(service);
				service.start();
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e, clazz.getSimpleName() + "安装模块服务器失败");
			}
			System.out.println("成功安装模块服务器：" + clazz.getSimpleName());
		}
	}
}