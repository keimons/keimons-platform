package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.ILogger;
import com.keimons.platform.iface.ILoggerConfig;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志服务
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.8
 */
public class LogService {

	/**
	 * 默认日志存放路径
	 */
	public static final String DEFAULT_LOG_PATH = "./logs/";

	private static Logger info;
	private static Logger warn;
	private static Logger debug;
	private static Logger error;

	public static Logger build(ILogger iLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger(iLogger.getName() + "Logger");
		// 设置不向上级打印信息
		logger.setAdditive(false);
		OutputStreamAppender<ILoggingEvent> appender = iLogger.build();
		logger.addAppender(appender);
		return logger;
	}

	/**
	 * 所有日志
	 */
	private static Map<Object, Logger> loggers = new HashMap<>();

	/**
	 * 打印日志
	 *
	 * @param logType 日志类型
	 * @param context 日志内容
	 */
	public static <T extends Enum<T> & ILoggerConfig> void log(T logType, String context) {
		Logger logger = loggers.get(logType.getLoggerName());
		if (logger != null) {
			logger.info(context);
		} else {
			error("尝试打印不存在的日志类型：" + logType);
		}
	}

	/**
	 * 打印日志
	 *
	 * @param logType 日志类型
	 * @param context 日志内容
	 */
	public static void log(String logType, String context) {
		Logger logger = loggers.get(logType);
		if (logger != null) {
			logger.info(context);
		} else {
			error("尝试打印不存在的日志类型：" + logType);
		}
	}

	/**
	 * 打印信息日志
	 *
	 * @param context 信息内容
	 */
	public static void info(String context) {
		if (info != null) {
			info.info(context);
		}
		System.out.println(context);
	}

	/**
	 * 打印警告日志
	 *
	 * @param context 警告内容
	 */
	public static void warn(String context) {
		if (warn != null) {
			warn.warn(context);
		}
		System.out.println(context);
	}

	/**
	 * 打印调试日志
	 *
	 * @param context 调试内容
	 */
	public static void debug(String context) {
		if (debug != null) {
			debug.debug(context);
		}
		System.out.println(context);
	}

	/**
	 * 打印错误日志
	 *
	 * @param context 错误内容
	 */
	public static void error(String context) {
		error(null, context);
	}

	/**
	 * 打印错误日志
	 *
	 * @param e 堆栈信息
	 */
	public static void error(Throwable e) {
		error(e, null);
	}

	/**
	 * 打印错误日志
	 *
	 * @param e       堆栈信息
	 * @param context 错误内容
	 */
	public static void error(Throwable e, String context) {
		if (e == null && context == null) {
			return;
		}
		if (context == null) {
			StringWriter trace = new StringWriter();
			e.printStackTrace(new PrintWriter(trace));
			context = trace.toString();
		} else {
			if (e != null) {
				StringWriter trace = new StringWriter();
				e.printStackTrace(new PrintWriter(trace));
				context = context + " " + trace.toString();
			}
		}
		if (error != null) {
			error.error(context);
		}
		System.err.println(context);
	}

	/**
	 * 初始化日志模块
	 *
	 * @param clazz 日志枚举类
	 * @param <T>   实现了{@link ILoggerConfig}接口的日志枚举类
	 * @deprecated 未完成的设计方案，谨慎使用
	 */
	@Deprecated
	public static <T extends Enum<T> & ILoggerConfig> void init(Class<T> clazz) {
		String logPath = System.getProperty(KeimonsServer.LOG_PATH, DEFAULT_LOG_PATH);
		init();
		if (clazz != null) {
			T[] values = clazz.getEnumConstants();
			for (T logType : values) {
				loggers.put(logType.getLoggerName(), build(new DefaultRollingFileLogger(logPath, logType.getLoggerName(), logType.getLoggerLevel())));
			}
		}
	}

	/**
	 * 初始化日志系统
	 */
	public static void init(String... logs) {
		String logPath = System.getProperty(KeimonsServer.LOG_PATH, DEFAULT_LOG_PATH);

		info = build(new DefaultRollingFileLogger(logPath, "info", Level.INFO));
		info("成功初始化[info]日志");

		debug = build(new DefaultRollingFileLogger(logPath, "debug", Level.DEBUG));
		info("成功初始化[debug]日志");

		warn = build(new DefaultRollingFileLogger(logPath, "warn", Level.WARN));
		info("成功初始化[warn]日志");

		error = build(new DefaultRollingFileLogger(logPath, "error", Level.ERROR));
		info("成功初始化[error]日志");

		for (String log : logs) {
			loggers.put(log, build(new DefaultRollingFileLogger(logPath, log, Level.INFO)));
		}
	}
}