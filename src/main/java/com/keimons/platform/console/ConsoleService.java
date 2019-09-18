package com.keimons.platform.console;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.keimons.platform.log.DefaultConsoleLogger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 * System.out和System.err重定向到Logback
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
public class ConsoleService extends PrintStream {

	public static final String OUT_CONSOLE = "OutConsole";
	public static final String ERR_CONSOLE = "ErrConsole";

	private final PrintStream printStream;
	private final ConsoleLogger logger;

	/**
	 * 重定向方案
	 *
	 * @param printStream 输出流
	 * @param logger      日志
	 */
	public ConsoleService(PrintStream printStream, ConsoleLogger logger) {
		super(new ByteArrayOutputStream());
		this.printStream = printStream;
		this.logger = logger;
	}

	@Override
	public synchronized void println(final String string) {
		logger.log(string);
	}

	@Override
	public synchronized void println(final Object object) {
		logger.log(String.valueOf(object));
	}

	@Override
	public synchronized void println() {
		logger.log("");
	}

	@Override
	public synchronized void println(final boolean value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void println(final char value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void println(final char[] array) {
		logger.log(String.valueOf(array));
	}

	@Override
	public synchronized void println(final double value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void println(final float value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void println(final int value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void println(final long value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized PrintStream append(final char character) {
		logger.log(String.valueOf(character));
		return this;
	}

	@Override
	public synchronized PrintStream append(final CharSequence csq, final int start, final int end) {
		logger.log(csq.subSequence(start, end).toString());
		return this;
	}

	@Override
	public synchronized PrintStream append(final CharSequence csq) {
		logger.log(csq.toString());
		return this;
	}

	@Override
	public boolean checkError() {
		return printStream.checkError();
	}

	@Override
	protected void setError() {
		printStream.println("WARNING - calling setError on SLFJPrintStream does nothing");
	}

	@Override
	public void close() {
		printStream.close();
	}

	@Override
	public void flush() {
		printStream.flush();
	}

	@Override
	public synchronized PrintStream format(final Locale locale, final String format, final Object... args) {
		final String string = String.format(locale, format, args);
		logger.log(string);
		return this;
	}

	@Override
	public synchronized PrintStream format(final String format, final Object... args) {
		return format(Locale.getDefault(), format, args);
	}

	@Override
	public synchronized void print(final boolean value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void print(final char value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void print(final char[] array) {
		logger.log(String.valueOf(array));
	}

	@Override
	public synchronized void print(final double value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void print(final float value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void print(final int value) {
		logger.log(String.valueOf(value));
	}

	@Override
	public synchronized void print(final long lon) {
		logger.log(String.valueOf(lon));
	}

	@Override
	public synchronized void print(final Object object) {
		logger.log(String.valueOf(object));
	}

	@Override
	public synchronized void print(final String string) {
		logger.log(string);
	}

	@Override
	public synchronized PrintStream printf(final Locale locale, final String format, final Object... args) {
		return format(locale, format, args);
	}

	@Override
	public synchronized PrintStream printf(final String format, final Object... args) {
		return format(format, args);
	}

	@Override
	public void write(final byte[] buf, final int off, final int len) {
		printStream.write(buf, off, len);
	}

	@Override
	public void write(final int integer) {
		printStream.write(integer);
	}

	@Override
	public void write(final byte[] bytes) throws IOException {
		printStream.write(bytes);
	}

	/**
	 * 重定向System.out和System.out到Logback
	 */
	public static void init() {
		// 初始化System.out和System.err日志
		init(ConsoleService.OUT_CONSOLE, "%blue([%d{yyyy-MM-dd HH:mm:ss.SSS}]) %msg%n", Level.INFO);
		init(ConsoleService.ERR_CONSOLE, "%red([%d{yyyy-MM-dd HH:mm:ss.SSS}]) %highlight(%msg%n)", Level.ERROR);

		// 重定向System.out和System.err到日志
		System.setOut(new ConsoleService(System.out, ConsoleLogger.INFO));
		System.setErr(new ConsoleService(System.err, ConsoleLogger.ERROR));
	}

	/**
	 * 重定向
	 *
	 * @param name    重定向日志
	 * @param pattern 输出格式
	 * @param level   输出等级
	 */
	private static void init(String name, String pattern, Level level) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = context.getLogger(name);
		// 设置不向上级打印信息
		logger.setAdditive(false);
		ConsoleAppender<ILoggingEvent> appender = (ConsoleAppender<ILoggingEvent>) new DefaultConsoleLogger(pattern, level).build();
		logger.addAppender(appender);
	}
}