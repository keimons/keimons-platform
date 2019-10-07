package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.ILogger;
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
 * @since 1.8
 */
public class LogService {

	/**
	 * 默认日志存放路径
	 */
	public static final String DEFAULT_LOG_PATH = "./logs/";

	/**
	 * 信息
	 */
	private static Logger info;

	/**
	 * 警告
	 */
	private static Logger warn;

	/**
	 * 调试
	 */
	private static Logger debug;

	/**
	 * 错误
	 */
	private static Logger error;

	/**
	 * 构造日志
	 *
	 * @param iLogger 日志信息
	 * @return 日志
	 */
	public static Logger build(ILogger iLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger(iLogger.getName() + "Logger");
		// 设置不向上级打印信息
		logger.setAdditive(false);
		logger.setLevel(Level.INFO);
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
	 * @param logName 日志名字 最终输出到硬盘上的日志名
	 * @param context 日志内容
	 */
	public static void log(String logName, String context) {
		Logger logger = loggers.get(logName);
		if (logger == null) {
			String logPath = System.getProperty(KeimonsServer.LOG_PATH, DEFAULT_LOG_PATH);
			logger = build(new DefaultRollingFileLogger(logPath, logName, Level.INFO));
			loggers.put(logName, logger);
		}
		logger.info(context);
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
	 * 初始化日志系统
	 */
	public static void init() {
		String logPath = System.getProperty(KeimonsServer.LOG_PATH, DEFAULT_LOG_PATH);

		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger root = context.getLogger("ROOT");
		root.setLevel(Level.WARN);

		if (info == null) {
			info = build(new DefaultRollingFileLogger(logPath, "info", Level.INFO));
			info("成功初始化[info]日志");
		}

		if (debug == null) {
			debug = build(new DefaultRollingFileLogger(logPath, "debug", Level.DEBUG));
			info("成功初始化[debug]日志");
		}

		if (debug == null) {
			warn = build(new DefaultRollingFileLogger(logPath, "warn", Level.WARN));
			info("成功初始化[warn]日志");
		}

		if (error == null) {
			error = build(new DefaultRollingFileLogger(logPath, "error", Level.ERROR));
			info("成功初始化[error]日志");
		}
	}
}