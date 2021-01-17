package com.keimons.platform.log;

/**
 * 日志实现接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface ILogger {

	/**
	 * Return the name of this {@link ILogger} instance.
	 *
	 * @return name of this logger instance
	 */
	String name();

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 *
	 * @return True if this Logger is enabled for the DEBUG level,
	 * false otherwise.
	 */
	boolean isDebugEnabled();

	/**
	 * Log a message at the DEBUG level.
	 *
	 * @param msg the message string to be logged
	 */
	void debug(String msg);

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level. </p>
	 *
	 * @param format the format string
	 * @param arg    the argument
	 */
	void debug(String format, Object arg);

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the DEBUG level. </p>
	 *
	 * @param format the format string
	 * @param arg0   the first argument
	 * @param arg1   the second argument
	 */
	void debug(String format, Object arg0, Object arg1);

	/**
	 * Log a message at the DEBUG level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous string concatenation when the logger
	 * is disabled for the DEBUG level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an {@code Object[]} before invoking the method,
	 * even if this logger is disabled for DEBUG. The variants taking
	 * {@link #debug(String, Object) one} and {@link #debug(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.</p>
	 *
	 * @param format    the format string
	 * @param arguments a list of 3 or more arguments
	 */
	void debug(String format, Object... arguments);

	/**
	 * Log an exception (throwable) at the ERROR level.
	 *
	 * @param cause the exception (throwable) to log
	 */
	void debug(Throwable cause);

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param cause the exception (throwable) to log
	 * @param msg   the message accompanying the exception
	 */
	void debug(Throwable cause, String msg);

	/**
	 * Log an exception (throwable) at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param cause  the exception (throwable) to log
	 * @param format the format string
	 * @param arg    the argument
	 */
	void debug(Throwable cause, String format, Object arg);

	void debug(Throwable cause, String format, Object arg0, Object arg1);

	void debug(Throwable cause, String format, Object... arguments);

	/**
	 * Is the logger instance enabled for the INFO level?
	 *
	 * @return True if this Logger is enabled for the INFO level,
	 * false otherwise.
	 */
	boolean isInfoEnabled();

	/**
	 * Log a message at the INFO level.
	 *
	 * @param msg the message string to be logged
	 */
	void info(String msg);

	/**
	 * Log a message at the INFO level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level. </p>
	 *
	 * @param format the format string
	 * @param arg    the argument
	 */
	void info(String format, Object arg);

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the INFO level. </p>
	 *
	 * @param format the format string
	 * @param arg0   the first argument
	 * @param arg1   the second argument
	 */
	void info(String format, Object arg0, Object arg1);

	/**
	 * Log a message at the INFO level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous string concatenation when the logger
	 * is disabled for the INFO level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an {@code Object[]} before invoking the method,
	 * even if this logger is disabled for INFO. The variants taking
	 * {@link #info(String, Object) one} and {@link #info(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.</p>
	 *
	 * @param format    the format string
	 * @param arguments a list of 3 or more arguments
	 */
	void info(String format, Object... arguments);

	/**
	 * Log an exception (throwable) at the ERROR level.
	 *
	 * @param cause the exception (throwable) to log
	 */
	void info(Throwable cause);

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param cause the exception (throwable) to log
	 * @param msg   the message accompanying the exception
	 */
	void info(Throwable cause, String msg);

	/**
	 * Log an exception (throwable) at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param cause  the exception (throwable) to log
	 * @param format the format string
	 * @param arg    the argument
	 */
	void info(Throwable cause, String format, Object arg);

	void info(Throwable cause, String format, Object arg0, Object arg1);

	void info(Throwable cause, String format, Object... arguments);

	/**
	 * Is the logger instance enabled for the WARN level?
	 *
	 * @return True if this Logger is enabled for the WARN level,
	 * false otherwise.
	 */
	boolean isWarnEnabled();

	/**
	 * Log a message at the WARN level.
	 *
	 * @param msg the message string to be logged
	 */
	void warn(String msg);

	/**
	 * Log a message at the WARN level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level. </p>
	 *
	 * @param format the format string
	 * @param arg    the argument
	 */
	void warn(String format, Object arg);

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the WARN level. </p>
	 *
	 * @param format the format string
	 * @param arg0   the first argument
	 * @param arg1   the second argument
	 */
	void warn(String format, Object arg0, Object arg1);

	/**
	 * Log a message at the WARN level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous string concatenation when the logger
	 * is disabled for the WARN level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an {@code Object[]} before invoking the method,
	 * even if this logger is disabled for WARN. The variants taking
	 * {@link #warn(String, Object) one} and {@link #warn(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.</p>
	 *
	 * @param format    the format string
	 * @param arguments a list of 3 or more arguments
	 */
	void warn(String format, Object... arguments);

	/**
	 * Log an exception (throwable) at the ERROR level.
	 *
	 * @param cause the exception (throwable) to log
	 */
	void warn(Throwable cause);

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param cause the exception (throwable) to log
	 * @param msg   the message accompanying the exception
	 */
	void warn(Throwable cause, String msg);

	/**
	 * Log an exception (throwable) at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param cause  the exception (throwable) to log
	 * @param format the format string
	 * @param arg    the argument
	 */
	void warn(Throwable cause, String format, Object arg);

	void warn(Throwable cause, String format, Object arg0, Object arg1);

	void warn(Throwable cause, String format, Object... arguments);

	/**
	 * Is the logger instance enabled for the ERROR level?
	 *
	 * @return True if this Logger is enabled for the ERROR level,
	 * false otherwise.
	 */
	boolean isErrorEnabled();

	/**
	 * Log a message at the ERROR level.
	 *
	 * @param msg the message string to be logged
	 */
	void error(String msg);

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param arg    the argument
	 */
	void error(String format, Object arg);

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param format the format string
	 * @param arg0   the first argument
	 * @param arg1   the second argument
	 */
	void error(String format, Object arg0, Object arg1);

	/**
	 * Log a message at the ERROR level according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous string concatenation when the logger
	 * is disabled for the ERROR level. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an {@code Object[]} before invoking the method,
	 * even if this logger is disabled for ERROR. The variants taking
	 * {@link #error(String, Object) one} and {@link #error(String, Object, Object) two}
	 * arguments exist solely in order to avoid this hidden cost.</p>
	 *
	 * @param format    the format string
	 * @param arguments a list of 3 or more arguments
	 */
	void error(String format, Object... arguments);

	/**
	 * Log an exception (throwable) at the ERROR level.
	 *
	 * @param cause the exception (throwable) to log
	 */
	void error(Throwable cause);

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param cause the exception (throwable) to log
	 * @param msg   the message accompanying the exception
	 */
	void error(Throwable cause, String msg);

	/**
	 * Log an exception (throwable) at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param cause  the exception (throwable) to log
	 * @param format the format string
	 * @param arg    the argument
	 */
	void error(Throwable cause, String format, Object arg);

	void error(Throwable cause, String format, Object arg0, Object arg1);

	void error(Throwable cause, String format, Object... arguments);

	/**
	 * Is the logger instance enabled for the specified {@code level}?
	 *
	 * @return True if this Logger is enabled for the specified {@code level},
	 * false otherwise.
	 */
	boolean isEnabled(Level level);

	/**
	 * Log a message at the specified {@code level}.
	 *
	 * @param msg the message string to be logged
	 */
	void log(Level level, String msg);

	/**
	 * Log a message at the specified {@code level} according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the specified {@code level}. </p>
	 *
	 * @param format the format string
	 * @param arg    the argument
	 */
	void log(Level level, String format, Object arg);

	/**
	 * Log a message at the specified {@code level} according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the specified {@code level}. </p>
	 *
	 * @param format the format string
	 * @param arg0   the first argument
	 * @param arg1   the second argument
	 */
	void log(Level level, String format, Object arg0, Object arg1);

	/**
	 * Log a message at the specified {@code level} according to the specified format
	 * and arguments.
	 * <p/>
	 * <p>This form avoids superfluous string concatenation when the logger
	 * is disabled for the specified {@code level}. However, this variant incurs the hidden
	 * (and relatively small) cost of creating an {@code Object[]} before invoking the method,
	 * even if this logger is disabled for the specified {@code level}. The variants taking
	 * {@link #log(Level, String, Object) one} and
	 * {@link #log(Level, String, Object, Object) two} arguments exist solely
	 * in order to avoid this hidden cost.</p>
	 *
	 * @param format    the format string
	 * @param arguments a list of 3 or more arguments
	 */
	void log(Level level, String format, Object... arguments);

	/**
	 * Log an exception (throwable) at the ERROR level.
	 *
	 * @param cause the exception (throwable) to log
	 */
	void log(Level level, Throwable cause);

	/**
	 * Log an exception (throwable) at the ERROR level with an
	 * accompanying message.
	 *
	 * @param cause the exception (throwable) to log
	 * @param msg   the message accompanying the exception
	 */
	void log(Level level, Throwable cause, String msg);

	/**
	 * Log an exception (throwable) at the ERROR level according to the specified format
	 * and argument.
	 * <p/>
	 * <p>This form avoids superfluous object creation when the logger
	 * is disabled for the ERROR level. </p>
	 *
	 * @param cause  the exception (throwable) to log
	 * @param format the format string
	 * @param arg    the argument
	 */
	void log(Level level, Throwable cause, String format, Object arg);

	void log(Level level, Throwable cause, String format, Object arg0, Object arg1);

	void log(Level level, Throwable cause, String format, Object... arguments);
}