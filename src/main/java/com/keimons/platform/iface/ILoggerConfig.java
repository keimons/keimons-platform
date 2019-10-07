package com.keimons.platform.iface;

import ch.qos.logback.classic.Level;

/**
 * 日志配置接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface ILoggerConfig {

	/**
	 * 获取日志名字
	 *
	 * @return 日志名字
	 */
	String getLoggerName();

	/**
	 * 获取日志级别
	 *
	 * @return 日志级别
	 */
	Level getLoggerLevel();
}