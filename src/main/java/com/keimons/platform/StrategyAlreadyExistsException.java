package com.keimons.platform;

import com.keimons.platform.unit.StringUtil;

/**
 * 策略已经存在异常
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class StrategyAlreadyExistsException extends RuntimeException {

	/**
	 * 策略已经存在
	 *
	 * @param strategy   策略
	 * @param strategyId 策略Id
	 */
	public StrategyAlreadyExistsException(String strategy, int strategyId) {
		super(StringUtil.format("\u0001 strategy code \u0001 already exists", strategy, strategyId));
	}
}