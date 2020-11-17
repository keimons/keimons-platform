package com.keimons.platform.log;

import java.util.logging.Logger;

/**
 * 内部实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class LoggerFactory {

	private static LoggerFactory factory;

	public static ILogger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	public static ILogger getLogger(String name) {
		if (factory == null) {
			try {
				factory = Slf4JLoggerFactory.INSTANCE;
				ILogger logger = factory.getLogger0(name);
				logger.debug("Using SLF4J as the default logging framework");
			} catch (Throwable ignore1) {
				factory = JdkLoggerFactory.INSTANCE;
				ILogger logger = factory.getLogger0(name);
				logger.debug("Using java.util.logging as the default logging framework");
			}
		}
		return factory.getLogger0(name);
	}

	/**
	 * 获取日志文件
	 *
	 * @param name 日志名称
	 * @return 日志文件
	 */
	abstract ILogger getLogger0(String name);

	/**
	 * 依赖于Slf4J的日志工厂
	 */
	static class Slf4JLoggerFactory extends LoggerFactory {

		static final Slf4JLoggerFactory INSTANCE = new Slf4JLoggerFactory();

		@Override
		public ILogger getLogger0(String name) {
			return new Slf4JLogger(org.slf4j.LoggerFactory.getILoggerFactory().getLogger(name));
		}
	}

	/**
	 * 依赖于Jdk的日志工厂
	 */
	static class JdkLoggerFactory extends LoggerFactory {

		static final JdkLoggerFactory INSTANCE = new JdkLoggerFactory();

		@Override
		public ILogger getLogger0(String name) {
			return new JdkLogger(Logger.getLogger(name));
		}
	}
}