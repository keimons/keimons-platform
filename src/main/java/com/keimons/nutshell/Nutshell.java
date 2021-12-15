package com.keimons.nutshell;

import com.keimons.nutshell.event.EventService;
import com.keimons.nutshell.log.ConsoleService;
import com.keimons.nutshell.modular.AssemblyManager;
import com.keimons.nutshell.module.BytesModuleSerialize;
import com.keimons.nutshell.network.KeimonsTcpService;
import com.keimons.nutshell.player.PlayerManager;
import com.keimons.nutshell.quartz.SchedulerService;
import org.jetbrains.annotations.NotNull;

/**
 * Nutshell模块集合
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class Nutshell {

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

		Nutshell.set(Optional.ADAPTER, null);
		Nutshell.set(Optional.SERIALIZE, new BytesModuleSerialize());
		Nutshell.set(Optional.NET, new KeimonsTcpService());
		Nutshell.set(Optional.MESSAGE_PARSE, null);

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