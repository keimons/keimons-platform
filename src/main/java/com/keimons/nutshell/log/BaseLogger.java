package com.keimons.nutshell.log;

/**
 * 基础日志实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class BaseLogger implements ILogger {

	private static final String EXCEPTION_MESSAGE = "Unexpected exception:";

	private final String name;

	protected BaseLogger(String name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isEnabled(Level level) {
		switch (level) {
			case DEBUG:
				return isDebugEnabled();
			case INFO:
				return isInfoEnabled();
			case WARN:
				return isWarnEnabled();
			case ERROR:
				return isErrorEnabled();
			default:
				throw new Error();
		}
	}

	@Override
	public void debug(Throwable t) {
		debug(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void info(Throwable t) {
		info(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void warn(Throwable t) {
		warn(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void error(Throwable t) {
		error(EXCEPTION_MESSAGE, t);
	}

	@Override
	public void log(Level level, String msg) {
		switch (level) {
			case DEBUG:
				debug(msg);
				break;
			case INFO:
				info(msg);
				break;
			case WARN:
				warn(msg);
				break;
			case ERROR:
				error(msg);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, String format, Object arg) {
		switch (level) {
			case DEBUG:
				debug(format, arg);
				break;
			case INFO:
				info(format, arg);
				break;
			case WARN:
				warn(format, arg);
				break;
			case ERROR:
				error(format, arg);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, String format, Object arg0, Object arg1) {
		switch (level) {
			case DEBUG:
				debug(format, arg0, arg1);
				break;
			case INFO:
				info(format, arg0, arg1);
				break;
			case WARN:
				warn(format, arg0, arg1);
				break;
			case ERROR:
				error(format, arg0, arg1);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, String format, Object... arguments) {
		switch (level) {
			case DEBUG:
				debug(format, arguments);
				break;
			case INFO:
				info(format, arguments);
				break;
			case WARN:
				warn(format, arguments);
				break;
			case ERROR:
				error(format, arguments);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, Throwable cause) {
		switch (level) {
			case DEBUG:
				debug(cause);
				break;
			case INFO:
				info(cause);
				break;
			case WARN:
				warn(cause);
				break;
			case ERROR:
				error(cause);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, Throwable cause, String msg) {
		switch (level) {
			case DEBUG:
				debug(msg, cause);
				break;
			case INFO:
				info(msg, cause);
				break;
			case WARN:
				warn(msg, cause);
				break;
			case ERROR:
				error(msg, cause);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, Throwable cause, String format, Object arg) {
		switch (level) {
			case DEBUG:
				debug(cause, format, arg);
				break;
			case INFO:
				info(cause, format, arg);
				break;
			case WARN:
				warn(cause, format, arg);
				break;
			case ERROR:
				error(cause, format, arg);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, Throwable cause, String format, Object arg0, Object arg1) {
		switch (level) {
			case DEBUG:
				debug(cause, format, arg0, arg1);
				break;
			case INFO:
				info(cause, format, arg0, arg1);
				break;
			case WARN:
				warn(cause, format, arg0, arg1);
				break;
			case ERROR:
				error(cause, format, arg0, arg1);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public void log(Level level, Throwable cause, String format, Object... arguments) {
		switch (level) {
			case DEBUG:
				debug(cause, format, arguments);
				break;
			case INFO:
				info(cause, format, arguments);
				break;
			case WARN:
				warn(cause, format, arguments);
				break;
			case ERROR:
				error(cause, format, arguments);
				break;
			default:
				throw new Error();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + '(' + name() + ')';
	}
}