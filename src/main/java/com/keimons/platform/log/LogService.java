package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.keimons.platform.iface.ILogType;
import com.keimons.platform.iface.ILogger;
import com.keimons.platform.unit.TimeUtil;
import org.slf4j.LoggerFactory;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * 日志服务
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 * @deprecated 临时解决方案，慎重使用
 */
@Deprecated
public class LogService {

	/**
	 * 日志文件路径
	 */
	private static String path;

	private static Logger console = build(new DefaultConsoleLogger());
	private static Logger info;
	private static Logger warn;
	private static Logger debug;
	private static Logger error;

	public static Logger build(ILogger iLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger("ROOT");
		// 设置不向上级打印信息
		logger.setAdditive(false);
		ConsoleAppender<ILoggingEvent> appender = (ConsoleAppender<ILoggingEvent>) logger.getAppender("console");
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		// 设置编码格式UTF-8
		encoder.setCharset(Charset.forName("UTF-8"));
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		encoder.setContext(context);
		// 设置格式
		encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %msg%n");
		// 启动
		encoder.start();
		appender.setEncoder(encoder);
		appender.start();
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

	public static void main(String[] args) {
		init(null, "./logs/");
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		System.out.println(111);
	}
}