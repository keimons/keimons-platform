package com.keimons.platform.log;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Jdk日志实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
class JdkLogger extends BaseLogger {

	static final String SELF = JdkLogger.class.getName();
	static final String SUPER = BaseLogger.class.getName();

	final Logger logger;

	JdkLogger(Logger logger) {
		super(logger.getName());
		this.logger = logger;
	}

	/**
	 * Log the message at the specified level with the specified throwable if any.
	 * This method creates a LogRecord and fills in caller date before calling
	 * this instance's JDK14 logger.
	 * <p>
	 * See bug report #13 for more details.
	 */
	private void log(Level level, String msg, Throwable t) {
		// millis and thread are filled by the constructor
		LogRecord record = new LogRecord(level, msg);
		record.setLoggerName(name());
		record.setThrown(t);
		fillCallerData(record);
		logger.log(record);
	}

	/**
	 * Fill in caller data if possible.
	 *
	 * @param record The record to update
	 */
	private static void fillCallerData(LogRecord record) {
		StackTraceElement[] steArray = new Throwable().getStackTrace();

		int selfIndex = -1;
		for (int i = 0; i < steArray.length; i++) {
			final String className = steArray[i].getClassName();
			if (className.equals(SELF) || className.equals(SUPER)) {
				selfIndex = i;
				break;
			}
		}

		int found = -1;
		for (int i = selfIndex + 1; i < steArray.length; i++) {
			final String className = steArray[i].getClassName();
			if (!(className.equals(SELF) || className.equals(SUPER))) {
				found = i;
				break;
			}
		}

		if (found != -1) {
			StackTraceElement ste = steArray[found];
			// setting the class name has the side effect of setting
			// the needToInferCaller variable to false.
			record.setSourceClassName(ste.getClassName());
			record.setSourceMethodName(ste.getMethodName());
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	@Override
	public void debug(String msg) {
		if (logger.isLoggable(Level.FINE)) {
			log(Level.FINE, msg, null);
		}
	}

	@Override
	public void debug(String format, Object arg) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void debug(String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg0, arg1);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void debug(String format, Object... arguments) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void debug(Throwable cause, String msg) {
		if (logger.isLoggable(Level.FINE)) {
			log(Level.FINE, msg, cause);
		}
	}

	@Override
	public void debug(Throwable cause, String format, Object arg) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg}, cause);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void debug(Throwable cause, String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg0, arg1}, cause);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void debug(Throwable cause, String format, Object... arguments) {
		if (logger.isLoggable(Level.FINE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arguments}, cause);
			log(Level.FINE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	@Override
	public void info(String msg) {
		if (logger.isLoggable(Level.INFO)) {
			log(Level.INFO, msg, null);
		}
	}

	@Override
	public void info(String format, Object arg) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void info(String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.format(format, arg0, arg1);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void info(String format, Object... arguments) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void info(Throwable cause, String msg) {
		if (logger.isLoggable(Level.INFO)) {
			log(Level.INFO, msg, cause);
		}
	}

	@Override
	public void info(Throwable cause, String format, Object arg) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg}, cause);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void info(Throwable cause, String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg0, arg1}, cause);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void info(Throwable cause, String format, Object... arguments) {
		if (logger.isLoggable(Level.INFO)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arguments}, cause);
			log(Level.INFO, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	@Override
	public void warn(String msg) {
		if (logger.isLoggable(Level.WARNING)) {
			log(Level.WARNING, msg, null);
		}
	}

	@Override
	public void warn(String format, Object arg) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void warn(String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.format(format, arg0, arg1);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void warn(String format, Object... arguments) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void warn(Throwable cause, String msg) {
		if (logger.isLoggable(Level.WARNING)) {
			log(Level.WARNING, msg, cause);
		}
	}

	@Override
	public void warn(Throwable cause, String format, Object arg) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg}, cause);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void warn(Throwable cause, String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg0, arg1}, cause);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void warn(Throwable cause, String format, Object... arguments) {
		if (logger.isLoggable(Level.WARNING)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arguments}, cause);
			log(Level.WARNING, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.SEVERE);
	}

	@Override
	public void error(String msg) {
		if (logger.isLoggable(Level.SEVERE)) {
			log(Level.SEVERE, msg, null);
		}
	}

	@Override
	public void error(String format, Object arg) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void error(String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.format(format, arg0, arg1);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void error(String format, Object... arguments) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void error(Throwable cause, String msg) {
		if (logger.isLoggable(Level.SEVERE)) {
			log(Level.SEVERE, msg, cause);
		}
	}

	@Override
	public void error(Throwable cause, String format, Object arg) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg}, cause);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void error(Throwable cause, String format, Object arg0, Object arg1) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arg0, arg1}, cause);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}

	@Override
	public void error(Throwable cause, String format, Object... arguments) {
		if (logger.isLoggable(Level.SEVERE)) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, new Object[]{arguments}, cause);
			log(Level.SEVERE, ft.getMessage(), ft.getThrowable());
		}
	}
}