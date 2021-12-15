package com.keimons.nutshell.log;

import com.keimons.nutshell.modular.IAssembly;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 * System.out和System.err重定向到Logback
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class ConsoleService implements IAssembly {

	/**
	 * 输出日志重定向
	 */
	public static final String OUT_CONSOLE = "OutConsole";

	/**
	 * 错误日志重定向
	 */
	public static final String ERR_CONSOLE = "ErrConsole";

	/**
	 * 是否初始化
	 */
	private boolean initialized = false;

	/**
	 * 重定向System.out和System.out到Logback
	 */
	@Override
	public void init() {
		if (!initialized) {
			synchronized (ConsoleService.class) {
				if (!initialized) {
					initialized = true;
					// 重定向System.out和System.err到日志
					System.setOut(new ConsoleStream(System.out, ConsoleLogger.INFO));
					System.setErr(new ConsoleStream(System.err, ConsoleLogger.ERROR));
				}
			}
		}
	}

	@Override
	public void start() throws Throwable {

	}

	@Override
	public void shutdown() {

	}

	static class ConsoleStream extends PrintStream {

		/**
		 * 输出流
		 */
		private final PrintStream printStream;

		/**
		 * 日志
		 */
		private final ConsoleLogger logger;

		public ConsoleStream(PrintStream printStream, ConsoleLogger logger) {
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
		public synchronized PrintStream format(final Locale locale, final @NotNull String format, final Object... args) {
			final String string = String.format(locale, format, args);
			logger.log(string);
			return this;
		}

		@Override
		public synchronized PrintStream format(final @NotNull String format, final Object... args) {
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
		public synchronized PrintStream printf(final Locale locale, final @NotNull String format, final Object... args) {
			return format(locale, format, args);
		}

		@Override
		public synchronized PrintStream printf(final @NotNull String format, final Object... args) {
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
}