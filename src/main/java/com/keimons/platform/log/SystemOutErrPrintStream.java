package com.keimons.platform.log;

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
public class SystemOutErrPrintStream extends PrintStream {

	public static final String OUT_CONSOLE = "OutConsole";
	public static final String ERR_CONSOLE = "ErrConsole";

	private final PrintStream printStream;
	private final SystemLogger logger;

	/**
	 * 重定向方案
	 *
	 * @param printStream 输出流
	 * @param logger      日志
	 */
	public SystemOutErrPrintStream(PrintStream printStream, SystemLogger logger) {
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
	public synchronized void println(final boolean bool) {
		logger.log(String.valueOf(bool));
	}

	@Override
	public synchronized void println(final char character) {
		logger.log(String.valueOf(character));
	}

	@Override
	public synchronized void println(final char[] charArray) {
		logger.log(String.valueOf(charArray));
	}

	@Override
	public synchronized void println(final double doub) {
		logger.log(String.valueOf(doub));
	}

	@Override
	public synchronized void println(final float floa) {
		logger.log(String.valueOf(floa));
	}

	@Override
	public synchronized void println(final int integer) {
		logger.log(String.valueOf(integer));
	}

	@Override
	public synchronized void println(final long lon) {
		logger.log(String.valueOf(lon));
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
}