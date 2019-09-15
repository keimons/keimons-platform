package com.keimons.platform.log;

import com.keimons.platform.iface.ILogType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEvent {

	/**
	 * 所属逻辑服
	 */
	private int serverId;

	/**
	 * 日志类型
	 */
	private Object logType;

	/**
	 * 消息体
	 */
	private String logContext;

	public <T extends Enum<T> & ILogType> LogEvent setLogType(T logType) {
		this.logType = logType;
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Enum<T> & ILogType> T getLogType() {
		return (T) logType;
	}
}