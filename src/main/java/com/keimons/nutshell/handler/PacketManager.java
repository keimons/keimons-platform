package com.keimons.nutshell.handler;

import com.keimons.nutshell.StrategyAlreadyExistsException;
import com.keimons.nutshell.basic.IPacketStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 包体策略管理
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class PacketManager {

	/**
	 * 包体解析策略
	 */
	private static final IPacketStrategy<?, ?>[] strategies = new IPacketStrategy[127];

	/**
	 * 获取一个包体解析策略
	 *
	 * @param strategyIndex 策略
	 * @return 包体解析策略
	 */
	@SuppressWarnings("unchecked")
	public static <PacketT, DataT, StrategyT extends IPacketStrategy<PacketT, DataT>>
	StrategyT getPacketStrategy(
			@Range(from = 0, to = 127) int strategyIndex) {
		return (StrategyT) strategies[strategyIndex];
	}

	/**
	 * 注册一个消息解析策略
	 *
	 * @param <PacketT>      包体类型
	 * @param <DataT>>       消息体类型
	 * @param strategyIndex  任务提交者
	 * @param packetStrategy 包体解析策略
	 */
	public synchronized static <PacketT, DataT> void registerPacketStrategy(
			@Range(from = 0, to = 127) int strategyIndex,
			@NotNull IPacketStrategy<PacketT, DataT> packetStrategy) {
		if (Objects.nonNull(strategies[strategyIndex])) {
			throw new StrategyAlreadyExistsException("packet", strategyIndex);
		}
		strategies[strategyIndex] = packetStrategy;
	}
}