package com.keimons.platform.module;

import com.keimons.platform.log.LogService;
import com.keimons.platform.player.IPlayer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 玩家数据加载器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Loader implements Runnable {

	/**
	 * 正在加载中的队列
	 */
	private static LinkedBlockingDeque<Runnable> loading = new LinkedBlockingDeque<>();

	/**
	 * 立即加载玩家数据（阻塞加载）
	 *
	 * @param player 玩家唯一标识
	 * @param <T>    玩家数据的唯一标识符的类型
	 * @return 玩家所有模块
	 */
	public static <T> BaseModules<T> fastLoad(IPlayer<T> player) {
		AtomicReference<BaseModules<T>> reference = new AtomicReference<>();
		FutureTask<Void> task = new FutureTask<>(player.getLoader(null, reference), null);
		loading.offerFirst(task);
		return reference.get();
	}

	/**
	 * 排队加载玩家数据
	 *
	 * @param player   玩家
	 * @param consumer 消费函数
	 * @param <T>      玩家唯一ID类型
	 */
	public static <T> void slowLoad(IPlayer<T> player, Consumer<IPlayer<T>> consumer) {
		loading.add(player.getLoader(consumer, null));
	}

	/**
	 * 排队加载
	 *
	 * @param runnable 执行过程
	 */
	public static void addLoad(Runnable runnable) {
		loading.add(runnable);
	}

	/**
	 * 阻塞加载
	 *
	 * @param callable 执行过程
	 * @param <R>      返回结果
	 * @return 返回值
	 * @throws ExecutionException   异常
	 * @throws InterruptedException 异常
	 */
	public static <R> R addLoad(Callable<R> callable) throws ExecutionException, InterruptedException {
		FutureTask<R> task = new FutureTask<>(callable);
		loading.offerFirst(task);
		return task.get();
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