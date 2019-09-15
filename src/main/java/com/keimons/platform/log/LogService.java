package com.keimons.platform.log;

import com.keimons.platform.iface.ILogType;
import com.keimons.platform.unit.TimeUtil;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 消息处理器
 */
public class LogService<T extends Enum<T> & ILogType> {
	private static LogService manager;

	public static LogService getInstance() {
		if (manager == null) {
			manager = new LogService<>();
		}
		return manager;
	}

	/**
	 * Key:ServerId, Value:Logger
	 */
	static Logger[] loggers;

	static Logger error = null;

	// RingBuffer 大小，必须是 2 的 N 次方
	private static final int RING_BUFFER_SIZE = 4 * 1024;

	private static Disruptor<LogEvent> disruptor;

	/**
	 * 打印日志
	 *
	 * @param logType    日志类型
	 * @param logContext 日志内容
	 */
	public static <T extends Enum<T> & ILogType> void log(int serverId, T logType, String logContext) {
		disruptor.publishEvent(LogService::translate, serverId, logType, logContext);
	}

	/**
	 * 打印日志
	 *
	 * @param e 堆栈信息
	 */
	public static void log(Exception e) {
		logError(e, null);
	}

	public void logInfo(String context) {

	}

	/**
	 * 打印日志
	 *
	 * @param e 堆栈信息
	 */
	public static void logError(Throwable e, String info) {
		if (error != null) {
			StringWriter trace = new StringWriter();
			e.printStackTrace(new PrintWriter(trace));
			error.error(TimeUtil.logDate() + " " + info + "\n" + trace.toString());
		}
	}

	private static <T extends Enum<T> & ILogType> void translate(LogEvent event, long sequence, int serverId, T logType, String logContext) {
		event.setServerId(serverId)
				.setLogType(logType)
				.setLogContext(logContext)
		;
	}

	/**
	 * 工厂方法
	 *
	 * @return
	 */
	private static LogEvent newInstance() {
		return new LogEvent();
	}

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			Class<T> clazz = (Class<T>) superclass.getActualTypeArguments()[0];
			T[] values = clazz.getEnumConstants();
			loggers = new Logger[values.length - 1];
			for (ILogType logType : values) {
				loggers[logType.getLogIndex()] = LoggerFactory.getLogger(logType.getLogger());
			}
		} else {
			throw new NullPointerException("未指定枚举参数");
		}

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

		disruptor = new Disruptor<>(LogService::newInstance,
				RING_BUFFER_SIZE, executor, ProducerType.MULTI,
				new SleepingWaitStrategy());

		EventHandler<LogEvent>[] handlers = new EventHandler[]{new LogEventHandler()};
		disruptor.handleEventsWith(handlers);

		disruptor.start();
	}

	public boolean startup() {
		this.init();
		return true;
	}

	public boolean shutdown() {
		disruptor.shutdown();
		return false;
	}

	public static void main(String[] args) {
		LogService<LogsEnum> logsEnumLogService = new LogService<>();
		logsEnumLogService.init();
	}
}