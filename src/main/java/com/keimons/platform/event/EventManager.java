package com.keimons.platform.event;

import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.iface.IEventHandler;
import com.keimons.platform.iface.IManager;
import com.keimons.platform.log.LogService;
import com.keimons.platform.player.AbsPlayer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
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
 */
public class EventManager implements IManager {

	/**
	 * RingBuffer 大小，必须是 2 的 N 次方
	 */
	public static final int RING_BUFFER_SIZE = 4 * 1024;

	private static Disruptor<Event> disruptor;

	/**
	 * 事件处理
	 */
	private static Map<IEventCode, Set<IEventHandler>> eventHandlers = new HashMap<>();

	/**
	 * 消息号处理
	 */
	public static IEventCodeExecute eventCodeExecute;

	/**
	 * 发布事件
	 *
	 * @param player    玩家uuid
	 * @param eventCode 事件号
	 * @param params    参数列表
	 */
	public static void publicEvent(AbsPlayer player, IEventCode eventCode, Object... params) {
		try {
			disruptor.publishEvent(EventManager::translate, player, eventCode, params);
		} catch (Exception e) {
			LogService.log(e);
		}
	}

	/**
	 * 发布事件
	 *
	 * @param player    玩家
	 * @param eventCode 事件号
	 * @param params    参数列表
	 */
	public static void publicEvent(AbsPlayer player, String eventCode, Object... params) {
		try {
			disruptor.publishEvent(EventManager::translate, player, eventCodeExecute.valueOf(eventCode), params);
		} catch (Exception e) {
			LogService.log(e);
		}
	}

	/**
	 * 注册事件处理器
	 *
	 * @param eventHandler
	 */
	public static void registerEvent(IEventHandler eventHandler) {
		for (IEventCode code : eventHandler.register()) {
			Set<IEventHandler> eventHandlers = EventManager.eventHandlers.computeIfAbsent(code, k -> new HashSet<>());
			eventHandlers.add(eventHandler);
		}
	}

	private static void translate(Event event, long sequence, AbsPlayer player, IEventCode eventCode, Object... parms) {
		event.setPlayer(player);
		event.setEventCode(eventCode);
		event.setParams(parms);
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		EventFactory<Event> eventFactory = new com.keimons.platform.event.EventFactory();
		ThreadFactory executor = Executors.defaultThreadFactory();

//		Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待新事件，这是策略模式的应用。
//		Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
//		例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
//		BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
//		SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
//		YieldingWaitStrategy 的性能是很好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
//		BusySpinWaitStrategy 是性能最高的等待策略，同时也是对部署环境要求最高的策略。这个性能最好用在事件处理线程比物理内核数目还要小的时候。例如：在禁用超线程技术的时候。
//		WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
//		WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
//		WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
//		WaitStrategy BUSYSPIN_WAIT = new BusySpinWaitStrategy();

//		ProducerType.SINGLE // 单一写者
//		ProducerType.MULTI  // 多个写者

		disruptor = new Disruptor<>(eventFactory,
				RING_BUFFER_SIZE, executor, ProducerType.MULTI,
				new BlockingWaitStrategy());

		EventHandler<Event>[] handlers = new EventHandler[]{EventManager::onEvent};
		disruptor.handleEventsWith(handlers);

		disruptor.start();
	}

	/**
	 * 事件处理
	 *
	 * @param object
	 * @param sequence
	 * @param endOfBatch
	 */
	private static void onEvent(Object object, long sequence, boolean endOfBatch) {
		Event event = (Event) object;
		Set<IEventHandler> handlers = EventManager.eventHandlers.get(event.getEventCode());
		if (handlers != null) {
			for (IEventHandler handler : handlers) {
				try {
					handler.handler(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void reload() {
		this.init();
	}

	@Override
	public boolean shutdown() {
		disruptor.shutdown();
		return true;
	}
}