package com.keimons.platform.iface;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

public interface ILogger {

	String getName();

	OutputStreamAppender<ILoggingEvent> build();
}