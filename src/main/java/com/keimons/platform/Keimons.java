package com.keimons.platform;

import com.keimons.platform.annotation.AManager;
import com.keimons.platform.annotation.AModular;
import com.keimons.platform.annotation.AService;
import com.keimons.platform.console.ConsoleService;
import com.keimons.platform.event.EventService;
import com.keimons.platform.game.GameDataManager;
import com.keimons.platform.iface.IEventHandler;
import com.keimons.platform.iface.IManager;
import com.keimons.platform.iface.IService;
import com.keimons.platform.log.LogService;
import com.keimons.platform.player.PlayerManager;
import com.keimons.platform.network.KeimonsTcpService;
import com.keimons.platform.network.coder.CodecAdapter;
import com.keimons.platform.process.HandlerManager;
import com.keimons.platform.quartz.SchedulerService;
import com.keimons.platform.unit.ClassUtil;
import com.keimons.platform.unit.TimeUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Keimons模块集合
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Keimons<T> {

	private Class<T> messageType;

	/**
	 * 所有的管理器
	 */
	Map<Class<?>, IManager> managers = new HashMap<>();

	/**
	 * 所有的服务器
	 */
	Map<Class<?>, IService> services = new HashMap<>();

	KeimonsConfig config;

	KeimonsTcpService<T> net;

	HandlerManager executor;

	public Keimons(KeimonsConfig config, CodecAdapter<T> adapter) {
		this.messageType = adapter.getMessageType();
		this.config = config;
		executor = new HandlerManager(messageType, adapter::getMsgCode);
		net = new KeimonsTcpService<>(adapter, executor);
	}

	public void start() {
		if (config.isConsoleRedirect()) {
			ConsoleService.init();
			System.out.println("启用控制台输出重定向！");
		} else {
			System.out.println("禁用控制台输出重定向！");
		}
		LogService.init();
		SchedulerService.init();
		EventService.init();
		PlayerManager.init();
		List<Package> packages = new ArrayList<>();
		for (Package pkg : ClassUtil.getPackages("")) {
			AModular modular = pkg.getAnnotation(AModular.class);
			if (modular != null) {
				packages.add(pkg);
			}
		}

		packages.sort((Comparator.comparingInt(pkg -> pkg.getAnnotation(AModular.class).Priority())));
		for (Package pkg : packages) {
			String packageName = pkg.getName();
			System.out.println("************************* 开始安装模块 *************************");
			AModular modular = pkg.getAnnotation(AModular.class);
			System.out.println("模块安装：" + modular.Name() + "，安装顺序：" + modular.Priority());
			// Logger、Processor、Job、PlayerData、GameData
			addManager(packageName);
			addService(packageName);
			executor.addProcessor(packageName);
			SchedulerService.addJobs(packageName);
			PlayerManager.addGameData(packageName);
			GameDataManager.addGameData(packageName);

			System.out.println("************************* 完成安装模块 *************************");
		}

		net.init();
	}

	/**
	 * 重新加载静态数据
	 */
	public void reload() {
		for (IManager manager : managers.values()) {
			manager.reload();
		}
	}

	/**
	 * 关闭入口
	 */
	public void shutdown() {
		// 关闭Netty
		System.out.println("服务器准备关闭！");
		net.close();
		long time = TimeUtil.currentTimeMillis();
		for (IService service : services.values()) {
			service.shutdown();
		}
		System.out.println("服务器关闭耗时：" + (TimeUtil.currentTimeMillis() - time));
	}

	/**
	 * 初始化管理
	 *
	 * @param packageName 包名
	 */
	private void addManager(String packageName) {
		List<Class<IManager>> list = ClassUtil.loadClasses(packageName, AManager.class);
		for (Class<IManager> clazz : list) {
			System.out.println("正在安装模块管理器：" + clazz.getSimpleName());
			try {
				IManager manager = clazz.getDeclaredConstructor().newInstance();
				managers.put(manager.getClass(), manager);
				manager.load();
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				LogService.error(e, clazz.getSimpleName() + "安装模块管理器失败");
			}
			System.out.println("成功安装模块管理器：" + clazz.getSimpleName());
		}
	}

	/**
	 * 初始化服务
	 *
	 * @param packageName 包名
	 */
	private void addService(String packageName) {
		List<Class<IService>> list = ClassUtil.loadClasses(packageName, AService.class);
		for (Class<IService> clazz : list) {
			System.out.println("正在安装模块服务器：" + clazz.getSimpleName());
			try {
				IService service = clazz.getDeclaredConstructor().newInstance();
				services.put(service.getClass(), service);
				if (service instanceof IEventHandler) {
					EventService.registerEvent((IEventHandler) service);
				}
				service.start();
			} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
				LogService.error(e, clazz.getSimpleName() + "安装模块服务器失败");
			}
			System.out.println("成功安装模块服务器：" + clazz.getSimpleName());
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends IManager> T getManager(Class<T> clazz) {
		return (T) managers.get(clazz);
	}

	/**
	 * 获取所有管理
	 *
	 * @return 所有管理
	 */
	public Collection<IManager> getManagers() {
		return managers.values();
	}

	@SuppressWarnings("unchecked")
	public <T extends IService> T getService(Class<T> clazz) {
		return (T) services.get(clazz);
	}

	/**
	 * 获取所有服务
	 *
	 * @return 所有服务
	 */
	public Collection<IService> getServices() {
		return services.values();
	}

	public static void main(String[] args) {
	}
}