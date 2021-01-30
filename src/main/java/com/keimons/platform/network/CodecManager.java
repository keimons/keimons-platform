package com.keimons.platform.network;

import com.keimons.basic.ICodecStrategy;
import com.keimons.platform.StrategyAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 编解码器管理
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-25
 * @since 11
 **/
public class CodecManager {

	/**
	 * 编解码器策略
	 */
	private static final ICodecStrategy<?, ?>[] strategies = new ICodecStrategy[127];

	/**
	 * 获取一个消息解析策略
	 *
	 * @param <InBound>     入栈消息类型
	 * @param <OutBound>    出栈消息类型
	 * @param <StrategyT>   解析器类型
	 * @param strategyIndex 策略
	 * @return 消息解析策略
	 */
	@SuppressWarnings("unchecked")
	public static <InBound, OutBound, StrategyT extends ICodecStrategy<InBound, OutBound>>
	StrategyT getCodecStrategy(@Range(from = 0, to = 127) int strategyIndex) {
		return (StrategyT) strategies[strategyIndex];
	}

	/**
	 * 注册一个消息解析策略
	 *
	 * @param <InBound>     入栈消息类型
	 * @param <OutBound>    出栈消息类型
	 * @param strategyIndex 任务提交者
	 * @param codecStrategy 消息解析策略
	 */
	public synchronized static <InBound, OutBound> void registerCodecStrategy(
			@Range(from = 0, to = 127) int strategyIndex,
			@NotNull ICodecStrategy<InBound, OutBound> codecStrategy) {
		if (Objects.nonNull(strategies[strategyIndex])) {
			throw new StrategyAlreadyExistsException("codec", strategyIndex);
		}
		strategies[strategyIndex] = codecStrategy;
	}
}