package com.keimons.platform.log;

import org.junit.Test;

/**
 * 日志测试
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class LogTest {

	private static final ILogger logger = LoggerFactory.getLogger(LogTest.class);

	@Test
	public void test() {
		logger.debug("debug log test! {} {} {}", 1, 2, 3);
		logger.info("info log test! {} {} {}", 1, 2, 3);
		logger.warn("warn log test! {} {} {}", 1, 2, 3);
		logger.error("error log test! {} {} {}", 1, 2, 3);
	}
}