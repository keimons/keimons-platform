package com.keimons.platform;

import com.keimons.platform.annotation.AManager;
import com.keimons.platform.annotation.AService;
import com.keimons.platform.console.ConsoleService;
import com.keimons.platform.event.EventService;
import com.keimons.platform.iface.IEventHandler;
import com.keimons.platform.iface.ILoggerConfig;
import com.keimons.platform.iface.IManager;
import com.keimons.platform.iface.IService;
import com.keimons.platform.log.LogService;
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

	public static <T extends Enum<T> & ILoggerConfig> void init(Class<T> logClass) {
		ConsoleService.init();
		checkConfig();
		LogService.init(logClass);
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
	public void start() {
		initManager();
		initService();
	}

	/**
	 * 关闭入口
	 */
	public static void shutdown() {
		// 关闭Netty
		System.out.println("服务器准备关闭！");
		KeimonsService netty = getService(KeimonsService.class);
		if (netty != null) {
			netty.close();
		}

		try {

			// 暂停15秒以便Netty处理完剩余逻辑
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long time = TimeUtil.currentTimeMillis();
		for (IManager manager : managers.values()) {
			manager.shutdown();
		}
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
				manager.init();
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
				service.startup();
				AService serviceInfo = clazz.getAnnotation(AService.class);
				System.out.println("服务: 加载顺序 " + serviceInfo.Priority() + "，名称：" + serviceInfo.Name() + "，描述：" + serviceInfo.Desc());
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e);
			}
		}

		System.out.println();
	}
}