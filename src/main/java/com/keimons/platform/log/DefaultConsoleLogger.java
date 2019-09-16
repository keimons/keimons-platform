package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.OutputStreamAppender;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 默认控制台日志实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class DefaultConsoleLogger extends BaseLogger {

	/**
	 * 控制台日志
	 */
	public DefaultConsoleLogger() {
		super(null, null, null);
		this.pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %msg%n";
	}

	@Override
	public OutputStreamAppender<ILoggingEvent> build() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();

		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		appender.setContext(context);

		LevelFilter levelFilter = DefaultLevelFilter.getLevelFilter(Level.INFO);
		levelFilter.start();
		appender.addFilter(levelFilter);

		// appender的name属性
		appender.setName("console");

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		// 设置编码格式UTF-8
		encoder.setCharset(Charset.forName("UTF-8"));
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		encoder.setContext(context);
		// 设置格式
		encoder.setPattern(getPattern());
		// 启动
		encoder.start();
		// 加入下面两个节点
		appender.setEncoder(encoder);
		// 启动
		appender.start();
		return appender;
	}
}
