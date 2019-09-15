package com.keimons.platform.log;

import ch.qos.logback.classic.Level;

/**
 * 默认日志
 *
 * @author monkey1993
 * @since 1.0
 */
public class DefaultLogger extends AbsLogger {

	/**
	 * 错误日志
	 *
	 * @param path 输出路径
	 * @param name 日志名称
	 */
	public DefaultLogger(String path, String name, Level level) {
		super(path, name, level);
	}
}
