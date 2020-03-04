package com.keimons.platform.log;

import java.util.ArrayList;
import java.util.List;

public class ThreadLocalErrorLogger {

	private static final ThreadLocal<List<String>> info = new ThreadLocal<>();

	/**
	 * 记录一条线程本地日志
	 *
	 * @param info   日志信息
	 * @param params 日志参数
	 */
	public void logger(String info, Object... params) {
		List<String> strings = ThreadLocalErrorLogger.info.get();
		if (strings == null) {
			strings = new ArrayList<>();
			ThreadLocalErrorLogger.info.set(strings);
		}
		strings.add(String.format(info, params));
	}

	public void getLog() {
	}
}