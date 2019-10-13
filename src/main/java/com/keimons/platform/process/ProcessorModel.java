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
	private static ProcessorExecutor[] midExecutors;

	/**
	 * 低执行耗时线程队列
	 */
	private static ProcessorExecutor[] lowExecutors;

	/**
	 * 初始化消息处理线程模型
	 */
	public static void init() {
		if (KeimonsServer.KeimonsConfig.getNetThreadCount()[0] <= 0) {
			throw new KeimonsConfigException(KeimonsConfig.NET_THREAD_COUNT);
		}

		int[] levels = KeimonsServer.KeimonsConfig.getNetThreadLevel();

		int midThreadCount = KeimonsServer.KeimonsConfig.getNetThreadCount()[1];
		if (midThreadCount > 0) {
			mid = (ThreadPoolExecutor) Executors.newFixedThreadPool(midThreadCount);
			midExecutors = new ProcessorExecutor[midThreadCount];
			for (int i = 0; i < midThreadCount; i++) {
				midExecutors[i] = new ProcessorExecutor();
				mid.execute(midExecutors[i]);
			}
		}

		int lowThreadCount = KeimonsServer.KeimonsConfig.getNetThreadCount()[2];
		if (lowThreadCount == 0 && levels[0] == -1 && levels[1] == -1) {
			throw new KeimonsConfigException(KeimonsConfig.NET_THREAD_COUNT);
		}
		if (lowThreadCount > 0) {
			low = (ThreadPoolExecutor) Executors.newFixedThreadPool(lowThreadCount);
			lowExecutors = new ProcessorExecutor[lowThreadCount];
			for (int i = 0; i < lowThreadCount; i++) {
				lowExecutors[i] = new ProcessorExecutor();
				low.execute(lowExecutors[i]);
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
	public static void addMidProcessor(Session session, ProcessorInfo info, Packet packet) {
		midExecutors[session.getSessionId() / midExecutors.length].add(
				() -> info.processor(session, packet)
		);
	}

	/**
	 * 新增一个低速消息
	 *
	 * @param session   会话
	 * @param info 消息处理器
	 * @param packet    消息体
	 */
	public static void addLowProcessor(Session session, ProcessorInfo info, Packet packet) {
		lowExecutors[session.getSessionId() / lowExecutors.length].add(
				() -> info.processor(session, packet)
		);
	}
}