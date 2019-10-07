package com.keimons.platform.console;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System.out和System.err重定向到Logback
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public enum ConsoleLogger {

	/**
	 * 输出日志 System.out
	 */
	INFO {
		@Override
		public void log(final String message) {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			Logger logger = context.getLogger(ConsoleService.OUT_CONSOLE);
			((ch.qos.logback.classic.Logger) logger).setLevel(Level.INFO);
			logger.info(message);
		}
	},

	/**
	 * 错误日志 System.err
	 */
	ERROR {
		@Override
		public void log(final String message) {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			Logger logger = context.getLogger(ConsoleService.ERR_CONSOLE);
			((ch.qos.logback.classic.Logger) logger).setLevel(Level.ERROR);
			logger.error(message);
		}
	};

	/**
	 * 输出日志
	 *
	 * @param message 信息
	 */
	public abstract void log(String message);
}