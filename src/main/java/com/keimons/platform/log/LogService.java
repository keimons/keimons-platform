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

/**
 * 日志服务
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class LogService {

	/**
	 * 日志文件路径
	 */
	private static String path;

	private static Logger console;
	private static Logger info;
	private static Logger warn;
	private static Logger debug;
	private static Logger error;

	public static LogService getInstance() {
		return null;
	}

	private static Logger build(ILogger iLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger(iLogger.getName() + "Appender");
		// 设置不向上级打印信息
		logger.setAdditive(false);
		logger.addAppender(iLogger.build());
		return logger;
	}

	/**
	 * 所有日志
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
	 * 打印控制台信息
	 *
	 * @param msg
	 */
	public static void console(String msg) {
		console.info(msg);
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
		if (error == null) {
			return;
		}
		if (e == null) {
			error.error(TimeUtil.logDate() + " " + context);
		} else {
			StringWriter trace = new StringWriter();
			e.printStackTrace(new PrintWriter(trace));
			error.error(TimeUtil.logDate() + " " + context + "\n" + trace.toString());
		}
	}

	/**
	 * 初始化日志模块
	 *
	 * @param clazz 日志枚举类
	 * @param path  日志路径
	 * @param <T>   实现了{@link ILogType}接口的日志枚举类
	 */
	public static <T extends Enum<T> & ILogType> void init(Class<T> clazz, String path) {
		LogService.path = path;

		console = build(new DefaultConsoleLogger());
		info = build(new DefaultRollingFileLogger(path, "info", Level.INFO));
		debug = build(new DefaultRollingFileLogger(path, "debug", Level.DEBUG));
		warn = build(new DefaultRollingFileLogger(path, "warn", Level.WARN));
		error = build(new DefaultRollingFileLogger(path, "error", Level.ERROR));

		if (clazz != null) {
			T[] values = clazz.getEnumConstants();
			loggers = new Logger[values.length - 1];
			for (T logType : values) {
				loggers[logType.ordinal()] = build(logType.getLogger());
			}
		}
	}
}