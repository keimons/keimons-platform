package com.keimons.nutshell.log;

/**
 * System.out和System.err重定向到Logback
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public enum ConsoleLogger {

	/**
	 * 输出日志 System.out
	 */
	INFO {
		@Override
		public void log(final String message) {
			ILogger logger = LoggerFactory.getLogger(ConsoleService.OUT_CONSOLE);
			logger.info(message);
		}
	},

	/**
	 * 错误日志 System.err
	 */
	ERROR {
		@Override
		public void log(final String message) {
			ILogger logger = LoggerFactory.getLogger(ConsoleService.ERR_CONSOLE);
			logger.error(message);
		}
	};

	/**
	 * 输出日志
	 *
	 * @param message 信息
	 */
	public abstract void log(String message);
}