package com.keimons.platform.event;

import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.iface.IEventHandler;
import com.keimons.platform.log.LogService;
import com.keimons.platform.player.BasePlayer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 事件处理器
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.8
 */
public class EventService {

	/**
	 * RingBuffer 大小，必须是 2 的 N 次方
	 */
	public static final int RING_BUFFER_SIZE = 8 * 1024;

	/**
	 * 缓冲区构建器
	 * <br />
	 * Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
	 * 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
	 * BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
	 * SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
	 * YieldingWaitStrategy 的性能是很好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
	 * BusySpinWaitStrategy 是性能最高的等待策略，同时也是对部署环境要求最高的策略。这个性能最好用在事件处理线程比物理内核数目还要小的时候。例如：在禁用超线程技术的时候。
	 * <br />
	 * ProducerType.SINGLE // 单一写者
	 * ProducerType.MULTI  // 多个写者
	 */
	private static Disruptor<Event> disruptor;

	/**
	 * 事件处理
	 */
	private static Map<String, Set<IEventHandler>> eventHandlers = new HashMap<>();

	/**
	 * 发布事件
	 *
	 * @param player    玩家
	 * @param eventCode 事件号
	 * @param params    参数列表
	 */
	public static <T extends Enum<T> & IEventCode> void publicEvent(BasePlayer player, T eventCode, Object... params) {
		try {
			disruptor.publishEvent(EventService::translate, player, eventCode, params);
		} catch (Exception e) {
			LogService.error(e);
		}
	}

	/**
	 * 注册事件处理器
	 *
	 * @param handler 处理器
	 */
	public static void registerEvent(IEventHandler handler) {
		for (IEventCode code : handler.register()) {
			Set<IEventHandler> handlers = EventService.eventHandlers.computeIfAbsent(code.toString(), k -> new HashSet<>());
			handlers.add(handler);
		}
	}

	/**
	 * 发布事件
	 *
	 * @param event     事件
	 * @param sequence  序列
	 * @param player    玩家
	 * @param eventCode 事件号
	 * @param params    参数
	 */
	private static <T extends Enum<T> & IEventCode> void translate(Event event, long sequence, BasePlayer player, T eventCode, Object... params) {
		event.setPlayer(player);
		event.setEventCode(eventCode);
		event.setParams(params);
	}

	/**
	 * 事件处理
	 *
	 * @param event      被发布的事件
	 * @param sequence   事件序列
	 * @param endOfBatch 是否最后一个
	 */
	private static void onEvent(Event event, long sequence, boolean endOfBatch) {
		Set<IEventHandler> handlers = EventService.eventHandlers.get(event.getEventCode().toString());
		if (handlers != null) {
			for (IEventHandler handler : handlers) {
				try {
					handler.handler(event);
				} catch (Exception e) {
					LogService.error(e);
				}
			}
		}
	}

	/**
	 * 初始化事件系统
	 */
	public static void init() {
		ThreadFactory executor = Executors.defaultThreadFactory();
		disruptor = new Disruptor<>(Event::new, RING_BUFFER_SIZE, executor, ProducerType.MULTI, new BlockingWaitStrategy());

		EventHandler eventHandler = (EventHandler<Event>) EventService::onEvent;
		disruptor.handleEventsWith(eventHandler);

		disruptor.start();
	}

	/**
	 * 关闭事件系统
	 */
	public static void shutdown() {
		disruptor.shutdown();
	}
}