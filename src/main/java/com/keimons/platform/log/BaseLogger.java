package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.OptionHelper;
import com.keimons.platform.iface.ILogger;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * 默认日志抽象实现
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.8
 */
@Setter
@Getter
public abstract class BaseLogger implements ILogger {

	private final String name;

	protected final Level level;

	private final String path;

	protected String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} %msg%n";

	public BaseLogger(String path, String name, Level level) {
		if (!path.endsWith("/")) {
			path += "/";
		}
		this.path = path;
		this.name = name;
		this.level = level;
	}

	@Override
	public OutputStreamAppender<ILoggingEvent> build() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		// 设置appender的
		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		appender.setContext(context);
		// appender的name属性
		appender.setName(name + "Appender");
		// 日志的文件名
		appender.setFile(OptionHelper.substVars(path + name + ".log", context));

		// 设置日志的级别过滤器
		LevelFilter levelFilter = DefaultLevelFilter.getLevelFilter(level);
		levelFilter.start();

		// 日志文件创建规则：时间线策略
		TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<>();
		// 文件名格式
		String fp = OptionHelper.substVars(path + "%d{yyyy-MM-dd}/" + name + ".%d{yyyy-MM-dd}.log", context);
		// 设置文件名模式
		policy.setFileNamePattern(fp);
		// 设置父节点是appender
		policy.setParent(appender);
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		policy.setContext(context);
		// 启动文件创建策略
		policy.start();

		// 日志输出样式和编码
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		encoder.setContext(context);
		// 设置格式
		encoder.setPattern(pattern);
		// 设置文件编码格式
		encoder.setCharset(Charset.forName("UTF-8"));
		// 启动编码器
		encoder.start();

		appender.setRollingPolicy(policy);
		appender.setEncoder(encoder);
		appender.addFilter(levelFilter);

		appender.start();
		return appender;
	}
}