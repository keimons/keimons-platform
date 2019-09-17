package com.keimons.platform.test;

import ch.qos.logback.classic.Level;
import com.keimons.platform.iface.ILoggerConfig;

/**
 * 测试日志枚举
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
public enum TestLogEnum implements ILoggerConfig {
	LOGIN("login"),
	LOGOUT("logout", Level.INFO);

	/**
	 * 日志类型
	 */
	private String loggerName;

	/**
	 * 日志级别
	 */
	private Level loggerLevel;

	TestLogEnum(String loggerName) {
		this(loggerName, Level.INFO);
	}

	TestLogEnum(String loggerName, Level loggerLevel) {
		this.loggerName = loggerName;
		this.loggerLevel = loggerLevel;
	}

	@Override
	public String getLoggerName() {
		return loggerName;
	}

	@Override
	public Level getLoggerLevel() {
		return loggerLevel;
	}
}
