package com.keimons.nutshell.log;

import org.slf4j.Logger;

/**
 * Slf4J日志实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
class Slf4JLogger extends BaseLogger {

	/**
	 * Slf4J日志实现
	 */
	private final transient Logger logger;

	/**
	 * 构造方法
	 *
	 * @param logger 日志实现
	 */
	Slf4JLogger(Logger logger) {
		super(logger.getName());
		this.logger = logger;
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(format, arg);
	}

	@Override
	public void debug(String format, Object arg0, Object arg1) {
		logger.debug(format, arg0, arg1);
	}

	@Override
	public void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
	}

	@Override
	public void debug(Throwable cause, String msg) {
		logger.debug(msg, cause);
	}

	@Override
	public void debug(Throwable cause, String format, Object arg) {
		logger.debug(String.format(format, arg), cause);
	}

	@Override
	public void debug(Throwable cause, String format, Object arg0, Object arg1) {
		logger.debug(String.format(format, arg0, arg1), cause);
	}

	@Override
	public void debug(Throwable cause, String format, Object... arguments) {
		logger.debug(String.format(format, arguments), cause);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(format, arg);
	}

	@Override
	public void info(String format, Object arg0, Object arg1) {
		logger.info(format, arg0, arg1);
	}

	@Override
	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	@Override
	public void info(Throwable cause, String msg) {
		logger.info(msg, cause);
	}

	@Override
	public void info(Throwable cause, String format, Object arg) {
		logger.info(String.format(format, arg), cause);
	}

	@Override
	public void info(Throwable cause, String format, Object arg0, Object arg1) {
		logger.info(String.format(format, arg0, arg1), cause);
	}

	@Override
	public void info(Throwable cause, String format, Object... arguments) {
		logger.info(String.format(format, arguments), cause);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(format, arg);
	}

	@Override
	public void warn(String format, Object arg0, Object arg1) {
		logger.warn(format, arg0, arg1);
	}

	@Override
	public void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
	}

	@Override
	public void warn(Throwable cause, String msg) {
		logger.warn(msg, cause);
	}

	@Override
	public void warn(Throwable cause, String format, Object arg) {
		logger.warn(String.format(format, arg), cause);
	}

	@Override
	public void warn(Throwable cause, String format, Object arg0, Object arg1) {
		logger.warn(String.format(format, arg0, arg1), cause);
	}

	@Override
	public void warn(Throwable cause, String format, Object... arguments) {
		logger.warn(String.format(format, arguments), cause);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(format, arg);
	}

	@Override
	public void error(String format, Object arg0, Object arg1) {
		logger.error(format, arg0, arg1);
	}

	@Override
	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	@Override
	public void error(Throwable cause, String msg) {
		logger.error(msg, cause);
	}

	@Override
	public void error(Throwable cause, String format, Object arg) {
		logger.error(String.format(format, arg), cause);
	}

	@Override
	public void error(Throwable cause, String format, Object arg0, Object arg1) {
		logger.error(String.format(format, arg0, arg1), cause);
	}

	@Override
	public void error(Throwable cause, String format, Object... arguments) {
		logger.error(String.format(format, arguments), cause);
	}
}