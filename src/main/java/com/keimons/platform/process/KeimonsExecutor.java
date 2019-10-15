package com.keimons.platform.process;

import com.keimons.platform.KeimonsConfig;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.exception.KeimonsConfigException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 业务处理线程模型
 * <p>
 * 业务处理线程共有3级线程模型分别是：
 * Netty自身的Work线程，执行时间小于100ms的二级线程，执行时间无限制的三级线程。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsExecutor {

	/**
	 * 中执行耗时线程队列
	 */
	private static Executor[] midExecutor;

	/**
	 * 低执行耗时线程队列
	 */
	private static Executor[] lowExecutor;

	private static Map<String, Executor> singleThreads = new HashMap<>();

	private static int MID_MAX;
	private static int LOW_MAX;

	/**
	 * 初始化消息处理线程模型
	 */
	public static void init() {
		if (KeimonsServer.KeimonsConfig.getNetThreadCount()[0] <= 0) {
			throw new KeimonsConfigException(KeimonsConfig.NET_THREAD_COUNT);
		}

		int[] levels = KeimonsServer.KeimonsConfig.getNetThreadLevel();

		MID_MAX = KeimonsServer.KeimonsConfig.getNetThreadCount()[1];
		if (MID_MAX > 0) {
			ThreadPoolExecutor mid = (ThreadPoolExecutor) Executors.newFixedThreadPool(MID_MAX);
			midExecutor = new Executor[MID_MAX];
			for (int i = 0; i < MID_MAX; i++) {
				midExecutor[i] = new Executor();
				mid.execute(midExecutor[i]);
			}
		}

		LOW_MAX = KeimonsServer.KeimonsConfig.getNetThreadCount()[2];
		if (LOW_MAX == 0 && levels[0] == -1 && levels[1] == -1) {
			throw new KeimonsConfigException(KeimonsConfig.NET_THREAD_COUNT);
		}
		if (LOW_MAX > 0) {
			ThreadPoolExecutor low = (ThreadPoolExecutor) Executors.newFixedThreadPool(LOW_MAX);
			lowExecutor = new Executor[LOW_MAX];
			for (int i = 0; i < LOW_MAX; i++) {
				lowExecutor[i] = new Executor();
				low.execute(lowExecutor[i]);
			}
		}

		for (String threadName : KeimonsServer.KeimonsConfig.getNetThreadNames()) {
			ThreadPoolExecutor single = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
			Executor queue = new Executor();
			single.execute(queue);
			singleThreads.put(threadName, queue);
		}
	}

	/**
	 * 新增一个高速消息
	 *
	 * @param session 会话
	 * @param info    消息处理器
	 * @param packet  消息体
	 */
	public static void asyncTopProcessor(Session session, ProcessorInfo info, Packet packet) {
		info.processor(session, packet);
	}

	/**
	 * 新增一个中速消息
	 *
	 * @param session 会话
	 * @param info    消息处理器
	 * @param packet  消息体
	 */
	public static void asyncMidProcessor(Session session, ProcessorInfo info, Packet packet) {
		int route = info.getRoute(session, packet, MID_MAX);
		midExecutor[route].add(() -> info.processor(session, packet));
	}

	/**
	 * 新增一个低速消息
	 *
	 * @param session 会话
	 * @param info    消息处理器
	 * @param packet  消息体
	 */
	public static void asyncLowProcessor(Session session, ProcessorInfo info, Packet packet) {
		int route = info.getRoute(session, packet, LOW_MAX);
		lowExecutor[route].add(() -> info.processor(session, packet));
	}

	/**
	 * 执行任务，并等待任务的执行结果
	 *
	 * @param threadName 线程名
	 * @param callable   执行消息体
	 * @param <T>        返回值类型
	 * @return 执行结果
	 * @throws ExecutionException   异常
	 * @throws InterruptedException 异常
	 */
	public static <T> T syncProcessor(String threadName, Callable<T> callable)
			throws ExecutionException, InterruptedException {
		return singleThreads.get(threadName).offer(callable);
	}

	/**
	 * 消息执行队列
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 **/
	static class Executor implements Runnable {

		private final BlockingDeque<Runnable> queue;

		public Executor() {
			queue = new LinkedBlockingDeque<>();
		}

		private boolean run = true;

		@Override
		public void run() {
			while (run) {
				try {
					Runnable reqs = queue.take();
					reqs.run();
				} catch (Throwable e) {
					LogService.error(e);
				}
			}
		}

		/**
		 * 增加一个任务
		 *
		 * @param runnable 队尾
		 */
		public void add(Runnable runnable) {
			queue.add(runnable);
		}

		/**
		 * 增加一个任务
		 *
		 * @param callable 队尾
		 * @return 是否成功
		 */
		public <T> T offer(Callable<T> callable) throws ExecutionException, InterruptedException {
			FutureTask<T> task = new FutureTask<>(callable);
			queue.offer(task);
			return task.get();
		}
	}
}