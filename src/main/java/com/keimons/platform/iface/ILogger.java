package com.keimons.platform.iface;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

/**
 * 日志接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public interface ILogger {

	String getName();

	OutputStreamAppender<ILoggingEvent> build();
}