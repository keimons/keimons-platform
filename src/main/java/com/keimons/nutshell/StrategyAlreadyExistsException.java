package com.keimons.nutshell;

import com.keimons.nutshell.unit.StringUtil;

/**
 * 策略已经存在异常
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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