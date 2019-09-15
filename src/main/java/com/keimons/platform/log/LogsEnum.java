package com.keimons.platform.log;

import com.keimons.platform.iface.ILogType;

public enum LogsEnum implements ILogType {
	ENUM;

	@Override
	public int getLogIndex() {
		return 0;
	}

	@Override
	public String getLogger() {
		return null;
	}
}
