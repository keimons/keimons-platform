package com.keimons.platform;

import java.util.Properties;

/**
 * Keimons-Platform的系统配置
 * <p>
 * 这里描述了整个平台系统的所有配置信息。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsConfig {

	/**
	 * 日志路径
	 */
	public static final String LOG_PATH = "keimons.log.path";

	/**
	 * 日志路径
	 */
	public static final String DEBUG = "keimons.debug";

	/**
	 * 日志路径
	 */
	public static final String CONSOLE_REDIRECT = "keimons.console.redirect";

	/**
	 * 默认日志路径
	 */
	public static final String DEFAULT_LOG_PATH = "./logs/";

	/**
	 * 是否Debug模式运行
	 */
	private boolean debug;

	/**
	 * 日志路径
	 */
	private String logPath;

	/**
	 * 控制台输出重定向
	 */
	private boolean consoleRedirect;

	/**
	 * 配置文件
	 *
	 * @param config 配置文件
	 */
	public KeimonsConfig(Properties config) {
		this.debug = Boolean.valueOf(config.getProperty(DEBUG, "true"));
		this.logPath = config.getProperty(LOG_PATH, DEFAULT_LOG_PATH);
		this.consoleRedirect = Boolean.valueOf(config.getProperty(CONSOLE_REDIRECT, "true"));
	}

	/**
	 * 启用默认配置
	 *
	 * @return 系统配置
	 */
	public static KeimonsConfig defaultConfig() {
		KeimonsConfig config = new KeimonsConfig();
		config.debug = true;
		config.logPath = "./logs/";
		return config;
	}

	/**
	 * 配置文件
	 */
	public static class KeimonsConfigBuilder {

		/**
		 * 是否Debug模式运行
		 */
		private boolean debug;

		/**
		 * 日志路径
		 */
		private String logPath;

		/**
		 * 生成配置文件
		 *
		 * @return 配置文件
		 */
		public KeimonsConfig build() {
			KeimonsConfig config = new KeimonsConfig();
			config.debug = debug;
			config.logPath = logPath;
			return config;
		}

		public boolean isDebug() {
			return debug;
		}

		public KeimonsConfigBuilder setDebug(boolean debug) {
			this.debug = debug;
			return this;
		}

		public String getLogPath() {
			return logPath;
		}

		public KeimonsConfigBuilder setLogPath(String logPath) {
			this.logPath = logPath;
			return this;
		}
	}

	private KeimonsConfig() {
	}

	public boolean isDebug() {
		return debug;
	}

	public String getLogPath() {
		return logPath;
	}

	public boolean isConsoleRedirect() {
		return consoleRedirect;
	}
}