package com.keimons.platform;

import com.keimons.platform.event.EventService;
import com.keimons.platform.log.ConsoleService;
import com.keimons.platform.modular.AssemblyManager;
import com.keimons.platform.module.BytesModuleSerialize;
import com.keimons.platform.network.KeimonsTcpService;
import com.keimons.platform.player.PlayerManager;
import com.keimons.platform.quartz.SchedulerService;
import org.jetbrains.annotations.NotNull;

/**
 * Keimons模块集合
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Keimons {

	public static void main(String[] args) {
		try {
			AssemblyManager.registerAssembly(ConsoleService.class, new ConsoleService());
			// 初始化
			AssemblyManager.init();
			// 启动
			AssemblyManager.startup();
			// 监听关闭
			Runtime.getRuntime().addShutdownHook(new Thread(AssemblyManager::shutdown));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		SchedulerService.init();
		EventService.init();
		PlayerManager.init();

		Keimons.set(Optional.ADAPTER, null);
		Keimons.set(Optional.SERIALIZE, new BytesModuleSerialize());
		Keimons.set(Optional.NET, new KeimonsTcpService());
		Keimons.set(Optional.MESSAGE_PARSE, null);

		EventService.init();
	}

	private static final Object[] optionals = new Object[64];

	public static synchronized <Z> void set(Optional<Z> option, @NotNull Z value) {
		optionals[option.ordinal()] = value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Optional<?> option) {
		return (T) optionals[option.ordinal()];
	}
}