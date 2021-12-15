package com.keimons.nutshell.player;

import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 玩家数据加载器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class PlayerLoader implements Runnable {

	private static final ILogger logger = LoggerFactory.getLogger(PlayerLoader.class);

	/**
	 * 正在加载中的队列
	 */
	private static LinkedBlockingDeque<Runnable> loading = new LinkedBlockingDeque<>();

	/**
	 * 排队加载玩家数据
	 *
	 * @param runnable 队列
	 */
	public static void slowLoad(Runnable runnable) {
		loading.add(runnable);
	}

	@Override
	public void run() {
		for (; ; ) {
			try {
				Runnable runnable = loading.take();
				runnable.run();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
}