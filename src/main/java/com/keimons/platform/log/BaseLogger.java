package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import com.keimons.platform.iface.ILogger;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 默认日志抽象实现
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
@Setter
@Getter
public abstract class BaseLogger implements ILogger {

	private final String name;

	protected final Level level;

	private final String path;

	protected FileSize maxFileSize = FileSize.valueOf("128MB");

	protected String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS}%msg%n";

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
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		//这里是可以用来设置appender的，在xml配置文件里面，是这种形式：
		// <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">
		RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
//        ConsoleAppender consoleAppender = new ConsoleAppender();

		//这里设置级别过滤器
		LevelFilter levelFilter = DefaultLevelFilter.getLevelFilter(level);
		levelFilter.start();
		appender.addFilter(levelFilter);


		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		// 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
		appender.setContext(context);
		// appender的name属性
		appender.setName(name + "Appender");
		//设置文件名
		appender.setFile(OptionHelper.substVars(path + name + ".log", context));

		appender.setAppend(true);

		appender.setPrudent(false);

		// 设置文件创建时间及大小的类
		SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
		// 文件名格式
		String fp = OptionHelper.substVars(path + format.format(new Date()) + "/" + name + ".%d{yyyy-MM-dd}.%i.log", context);
		// 最大日志文件大小
		policy.setMaxFileSize(maxFileSize);
		// 设置文件名模式
		policy.setFileNamePattern(fp);

		// 设置最大历史记录为15条
		// policy.setMaxHistory(15);

		// 总大小限制
		// policy.setTotalSizeCap(FileSize.valueOf("32GB"));

		// 设置父节点是appender
		policy.setParent(appender);
		// 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		// 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
		policy.setContext(context);
		policy.start();

		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		//设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
		// 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
		encoder.setContext(context);
		//设置格式
		encoder.setPattern(pattern);
		encoder.start();
		encoder.setCharset(Charset.forName("UTF-8"));

		//加入下面两个节点
		appender.setRollingPolicy(policy);
		appender.setEncoder(encoder);
		appender.start();
		return appender;
	}
}