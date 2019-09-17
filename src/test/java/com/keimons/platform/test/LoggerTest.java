package com.keimons.platform.test;

import com.keimons.platform.log.LogService;

/**
 * 日志测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-17
 * @since 1.0
 */
public class LoggerTest {

	public static void main(String[] args) {
		LogService.init(TestLogEnum.class, LogService.DEFAULT_LOG_PATH);
		LogService.log(TestLogEnum.LOGIN, "This is monkey1993 login.");
		LogService.log(TestLogEnum.LOGOUT, "This is monkey1993 logout.");
		LogService.log("levelup", "This is monkey1993 logout.");
		LogService.info("This is monkey1993 info.");
		LogService.warn("This is monkey1993 warn.");
		LogService.debug("This is monkey1993 debug.");
		LogService.error("This is monkey1993 error.");
		LogService.error(new NullPointerException());
		LogService.error(new NullPointerException("Null Point Exception"), "NULL POINT EXCEPTION");
	}
}