package com.keimons.platform.log;

import ch.qos.logback.classic.Level;

/**
 * 默认日志实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class DefaultRollingFileLogger extends BaseLogger {

	/**
	 * 错误日志
	 *
	 * @param path  输出路径
	 * @param name  日志名称
	 * @param level 日志级别
	 */
	public DefaultRollingFileLogger(String path, String name, Level level) {
		super(path, name, level);
	}
}
