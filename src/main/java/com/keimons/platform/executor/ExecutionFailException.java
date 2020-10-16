package com.keimons.platform.executor;

import com.keimons.platform.unit.StringUtil;

/**
 * 任务执行失败异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ExecutionFailException extends RuntimeException {

	/**
	 * 构造任务执行失败异常
	 *
	 * @param cause   异常原因
	 * @param message 异常信息
	 * @param params  信息参数
	 */
	public ExecutionFailException(Throwable cause, String message, Object... params) {
		super(StringUtil.format(message, params), cause);
	}

	/**
	 * 构造任务执行失败异常
	 *
	 * @param cause 异常原因
	 */
	public ExecutionFailException(Throwable cause) {
		super(cause);
	}
}