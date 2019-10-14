package com.keimons.platform.process;

import com.keimons.platform.KeimonsConfig;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.exception.KeimonsConfigException;
import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
public class ProcessorModel {

	/**
	 * 中执行耗时线程队列
	 */
	private static ThreadPoolExecutor mid;

	/**
	 * 低执行耗时线程队列
	 */
	private static ThreadPoolExecutor low;

	/**
	 * 中执行耗时线程队列
	 */
	private static ProcessorQueue[] midProcessorQueue;

	/**
	 * 低执行耗时线程队列
	 */
	private static ProcessorQueue[] lowProcessorQueue;

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
			mid = (ThreadPoolExecutor) Executors.newFixedThreadPool(MID_MAX);
			midProcessorQueue = new ProcessorQueue[MID_MAX];
			for (int i = 0; i < MID_MAX; i++) {
				midProcessorQueue[i] = new ProcessorQueue();
				mid.execute(midProcessorQueue[i]);
			}
		}

		LOW_MAX = KeimonsServer.KeimonsConfig.getNetThreadCount()[2];
		if (LOW_MAX == 0 && levels[0] == -1 && levels[1] == -1) {
			throw new KeimonsConfigException(KeimonsConfig.NET_THREAD_COUNT);
		}
		if (LOW_MAX > 0) {
			low = (ThreadPoolExecutor) Executors.newFixedThreadPool(LOW_MAX);
			lowProcessorQueue = new ProcessorQueue[LOW_MAX];
			for (int i = 0; i < LOW_MAX; i++) {
				lowProcessorQueue[i] = new ProcessorQueue();
				low.execute(lowProcessorQueue[i]);
			}
		}
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
		midProcessorQueue[route].add(() -> info.processor(session, packet));
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
		lowProcessorQueue[route].add(() -> info.processor(session, packet));
	}
}