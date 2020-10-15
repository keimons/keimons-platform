package com.keimons.platform.process;

/**
 * 处理器未找到异常
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-09-14
 * @since 1.8
 **/
public class HandlerNotFoundException extends RuntimeException {

	public HandlerNotFoundException(int msgCode) {
		super("msg code " + msgCode + " handler not found");
	}
}