package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.keimons.platform.iface.ILogType;
import com.keimons.platform.iface.ILogger;
import com.keimons.platform.unit.TimeUtil;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 消息处理器
 */
public class LogService<T extends Enum<T> & ILogType> {

	/**
	 * 日志文件路径
	 */
	private String path;

	private static Logger info;
	private static Logger warn;
	private static Logger debug;
	private static Logger error;

	private static LogService manager;

	public static LogService getInstance() {
		if (manager == null) {
			manager = new LogService<>();
		}
		return manager;
	}

	private Logger build(ILogger iLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger(iLogger.getName() + "Appender");
		//设置不向上级打印信息
		logger.setAdditive(false);
		logger.addAppender(iLogger.build());
		return logger;
	}

	/**
	 * Key:ServerId, Value:Logger
	 */
	private static Logger[] loggers;

	/**
	 * 打印日志
	 *
	 * @param logType 日志类型
	 * @param context 日志内容
	 */
	public static <T extends Enum<T> & ILogType> void log(T logType, String context) {
		loggers[logType.ordinal()].info(context);
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
		info.info(context);
	}

	public void logWarn(String context) {
		warn.warn(context);
	}

	public void logDebug(String context) {
		debug.debug(context);
	}

	public void logError(String context) {
		error.error(context);
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

	/**
	 * 初始化
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		info = build(new DefaultLogger(path, "info", Level.INFO));
		debug = build(new DefaultLogger(path, "debug", Level.DEBUG));
		warn = build(new DefaultLogger(path, "warn", Level.WARN));
		error = build(new DefaultLogger(path, "error", Level.ERROR));

		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			Class<T> clazz = (Class<T>) superclass.getActualTypeArguments()[0];
			T[] values = clazz.getEnumConstants();
			loggers = new Logger[values.length - 1];
			for (T logType : values) {
				loggers[logType.ordinal()] = build(logType.getLogger());
			}
		} else {
			throw new NullPointerException("未指定枚举参数");
		}
	}

	public boolean startup() {
		this.init();
		return true;
	}

	public boolean shutdown() {
		return false;
	}
}