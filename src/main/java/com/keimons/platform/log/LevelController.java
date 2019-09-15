package com.keimons.platform.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.core.spi.FilterReply;

class LevelController {

	/**
	 * 通过level设置日志过滤器
	 *
	 * @param level 级别
	 * @return 过滤器
	 */
	static LevelFilter getLevelFilter(Level level) {
		LevelFilter filter = new LevelFilter();
		filter.setLevel(level);
		// 日志匹配成功 立即处理日志
		filter.setOnMatch(FilterReply.ACCEPT);
		// 日志匹配失败 立即丢弃日志
		filter.setOnMismatch(FilterReply.DENY);
		return filter;
	}
}