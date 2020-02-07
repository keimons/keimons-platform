package com.keimons.platform.player;

import com.keimons.platform.log.LogService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

/**
 * 玩家数据加载器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class PlayerLoader implements Runnable {

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
				LogService.error(e);
			}
		}
	}
}