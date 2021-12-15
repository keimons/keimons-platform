package com.keimons.nutshell.executor;

import com.keimons.nutshell.unit.StringUtil;

/**
 * 任务执行失败异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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