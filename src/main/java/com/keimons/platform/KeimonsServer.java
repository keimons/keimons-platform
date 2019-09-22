package com.keimons.platform;

import com.keimons.platform.annotation.AManager;
import com.keimons.platform.annotation.AService;
import com.keimons.platform.console.ConsoleService;
import com.keimons.platform.event.EventService;
import com.keimons.platform.iface.IEventHandler;
import com.keimons.platform.iface.IManager;
import com.keimons.platform.iface.IService;
import com.keimons.platform.log.LogService;
import com.keimons.platform.quartz.QuartzService;
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
	 * 日志路径
	 */
	public static final String LOG_PATH = "LogPath";

	/**
	 * 日志路径
	 */
	public static final String PACKET = "Packet";

	/**
	 * 日志路径
	 */
	public static final String DEFAULT_PACKET = ".";

	/**
	 * 日志路径
	 */
	public static final String KEIMONS_PACKET = "com.keimons.platform";

	/**
	 * 控制台输出重定向
	 */
	public static final String KEIMONS_CONSOLE_REDIRECT = "ConsoleRedirect";

	/**
	 * 服务器ID （游戏服，世界服）
	 */
	public static int ServerId;

	/**
	 * 是否Debug模式
	 */
	public static boolean Debug;

	/**
	 * 运行中
	 */
	public static boolean RUNNING = true;

	/**
	 * 包名 程序运行中会扫描这个包下的文件
	 */
	public static String PackageName = ".";

	/**
	 * 初始化，初始化所有服务
	 *
	 * @param logs 日志
	 */
	public static void init(String... logs) {
		checkConfig();
		LogService.init(logs);
	}

	public static void checkConfig() {
		if (System.getProperty(LOG_PATH) == null) {
			System.out.println("未配置的[日志目录]，默认目录：" + LogService.DEFAULT_LOG_PATH);
		}
	}

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
	 */
	public static void start() {
		if (System.getProperty(KEIMONS_CONSOLE_REDIRECT, String.valueOf(true)).equals("true")) {
			ConsoleService.init();
			System.out.print("启用控制台输出重定向！");
		} else {
			System.out.println("禁用控制台输出重定向！");
		}
		LogService.init();
		EventService.init();
		QuartzService.init();
		initManager();
		initService();
		KeimonsTcpNet.init();
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
	private static void initManager() {
		List<Class<IManager>> list = ClassUtil.load(PackageName, AManager.class, IManager.class);

		list.sort(Comparator.comparingInt((Class<?> o) -> o.getAnnotation(AManager.class).Priority()));

		for (Class<IManager> clazz : list) {
			try {
				IManager manager = clazz.newInstance();
				managers.put(manager.getClass(), manager);
				if (manager instanceof IEventHandler) {
					EventService.registerEvent((IEventHandler) manager);
				}
				manager.load();
				AManager managerInfo = clazz.getAnnotation(AManager.class);
				System.out.println("管理器: 加载顺序 " + managerInfo.Priority() + "，名称：" + managerInfo.Name() + "，描述：" + managerInfo.Desc());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		System.out.println();
	}

	/**
	 * 初始化服务
	 */
	private static void initService() {
		List<Class<IService>> list = ClassUtil.load(PackageName, AService.class, IService.class);

		list.sort(Comparator.comparingInt((Class<?> o) -> o.getAnnotation(AService.class).Priority()));

		for (Class<IService> clazz : list) {
			try {
				IService service = clazz.newInstance();
				services.put(service.getClass(), service);
				if (service instanceof IEventHandler) {
					EventService.registerEvent((IEventHandler) service);
				}
				service.start();
				AService serviceInfo = clazz.getAnnotation(AService.class);
				System.out.println("服务: 加载顺序 " + serviceInfo.Priority() + "，名称：" + serviceInfo.Name() + "，描述：" + serviceInfo.Desc());
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e);
			}
		}

		System.out.println();
	}
}