package com.keimons.platform;

import com.keimons.platform.exception.KeimonsConfigException;
import groovy.lang.GroovyShell;

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
	 * 默认日志路径
	 */
	public static final String DEFAULT_LOG_PATH = "./logs/";

	/**
	 * 是否调试模式运行
	 */
	public static final String DEBUG = "keimons.debug";

	/**
	 * 默认是否调试模式运行
	 */
	public static final String DEFAULT_DEBUG = "true";

	/**
	 * 是否启用控制台重定向
	 */
	public static final String CONSOLE_REDIRECT = "keimons.console.redirect";

	/**
	 * 默认是否启用控制台重定向
	 */
	public static final String DEFAULT_CONSOLE_REDIRECT = "true";

	/**
	 * 是否自适应逻辑处理线程级别
	 */
	public static final String NET_THREAD_AUTO = "keimons.net.thread.auto";

	/**
	 * 默认是否自适应逻辑处理线程级别
	 */
	public static final String DEFAULT_NET_THREAD_AUTO = "true";

	/**
	 * 端口号
	 */
	public static final String NET_POTR = "keimons.net.port";

	/**
	 * 默认端口号
	 */
	public static final String DEFAULT_NET_POTR = "6364";

	/**
	 * 线程数量
	 */
	public static final String NET_THREAD_COUNT = "keimons.net.thread.count";

	/**
	 * 默认日志路径
	 */
	public static final String DEFAULT_NET_THREAD_COUNT = "cpu+1,cpu*2,cpu*3";

	/**
	 * 多级线程池
	 */
	public static final String NET_THREAD_LEVEL = "keimons.net.thread.level";

	/**
	 * 默认多级线程池
	 */
	public static final String DEFAULT_NET_THREAD_LEVEL = "20,50";

	/**
	 * 多级线程池
	 */
	public static final String NET_THREAD_SIMPLE = "keimons.net.thread.simple";

	/**
	 * 默认多级线程池
	 */
	public static final String DEFAULT_NET_THREAD_SIMPLE = "";

	/**
	 * 是否启用Debug模式运行
	 */
	private boolean debug;

	/**
	 * 日志路径
	 */
	private String logPath;

	/**
	 * 是否启用控制台输出重定向
	 */
	private boolean consoleRedirect;

	/**
	 * 端口号
	 */
	private int port;

	/**
	 * 是否启动多级线程池
	 */
	private boolean autoThreadLevel;

	/**
	 * 高中低线程的线程数量
	 */
	private int[] netThreadCount = new int[3];

	/**
	 * 跳线程的线程运行级别
	 */
	private int[] netThreadLevel = new int[2];

	/**
	 * 单线程线程名字
	 */
	private String[] netThreadNames;

	/**
	 * 配置文件
	 *
	 * @param config 配置文件
	 */
	public KeimonsConfig(Properties config) {
		String property;
		// 是否Debug模式运行
		property = config.getProperty(DEBUG, DEFAULT_DEBUG);
		this.debug = Boolean.valueOf(property);

		// 日志路径
		property = config.getProperty(LOG_PATH, DEFAULT_LOG_PATH);
		this.logPath = property;

		// 是否控制台重定向
		property = config.getProperty(CONSOLE_REDIRECT, DEFAULT_CONSOLE_REDIRECT);
		this.consoleRedirect = Boolean.valueOf(property);

		// 是否自适应逻辑线程运行级别
		property = config.getProperty(NET_THREAD_AUTO, DEFAULT_NET_THREAD_AUTO);
		this.autoThreadLevel = Boolean.valueOf(property);

		// 线程数量
		property = config.getProperty(NET_THREAD_COUNT, DEFAULT_NET_THREAD_COUNT);
		String[] counts = property.split(",");
		for (int i = 0; i < counts.length; i++) {
			netThreadCount[i] = getThreadCount(counts[i]);
		}

		if (netThreadCount[0] == 0) {
			throw new KeimonsConfigException(NET_THREAD_COUNT);
		}

		// 多级线程池
		property = config.getProperty(NET_THREAD_LEVEL, DEFAULT_NET_THREAD_LEVEL);
		String[] levels = property.split(",");
		for (int i = 0; i < levels.length && i < counts.length; i++) {
			netThreadLevel[i] = Integer.parseInt(levels[i]);
		}

		property = config.getProperty(NET_THREAD_LEVEL, DEFAULT_NET_THREAD_LEVEL);
		if (!property.equals("")) {
			String[] names = property.split(",");
			netThreadNames = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				netThreadNames[i] = names[i];
			}
		}

		// 端口号
		property = config.getProperty(NET_POTR, DEFAULT_NET_POTR);
		this.port = Integer.parseInt(property);
	}

	/**
	 * 获取线程数量
	 *
	 * @param expression 表达式 允许的特殊字符 "cpu"
	 * @return 线程数量
	 */
	public static int getThreadCount(String expression) {
		if (expression.equals("0")) {
			return 0;
		}
		String cpu = String.valueOf(Runtime.getRuntime().availableProcessors());
		expression = expression.trim().replaceAll(" ", "").replace("cpu", cpu);
		GroovyShell shell = new GroovyShell();
		return Math.max(1, (int) shell.evaluate(expression));
	}

	/**
	 * 启用默认配置
	 *
	 * @return 系统配置
	 */
	public static KeimonsConfig defaultConfig() {
		return new KeimonsConfig(new Properties());
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

	public boolean isAutoThreadLevel() {
		return autoThreadLevel;
	}

	public int getPort() {
		return port;
	}

	public int[] getNetThreadCount() {
		return netThreadCount;
	}

	public int[] getNetThreadLevel() {
		return netThreadLevel;
	}

	public String[] getNetThreadNames() {
		return netThreadNames;
	}
}