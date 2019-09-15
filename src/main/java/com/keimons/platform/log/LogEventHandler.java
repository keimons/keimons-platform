package com.keimons.platform.log;

import com.lmax.disruptor.EventHandler;

public class LogEventHandler implements EventHandler<LogEvent> {

	@Override
	public void onEvent(LogEvent event, long sequence, boolean endOfBatch) {
		LogService.loggers[event.getLogType().getLogIndex()].info(event.getLogContext());
	}
}