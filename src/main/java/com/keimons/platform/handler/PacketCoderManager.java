package com.keimons.platform.handler;

import com.keimons.platform.StrategyAlreadyExistsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public class PacketCoderManager {

	/**
	 * 消息解析策略
	 */
	private static final ICoderStrategy<?>[] parserStrategies = new ICoderStrategy[127];

	/**
	 * 包体解析策略
	 */
	private static final IPacketStrategy<?, ?>[] packetStrategies = new IPacketStrategy[127];

	static {
		registerPacketParserStrategy(0, new JsonCoderPolicy(), new JsonPacketPolicy());
	}

	/**
	 * 获取一个消息解析策略
	 *
	 * @param strategyIndex 策略
	 * @return 消息解析策略
	 */
	@SuppressWarnings("unchecked")
	public static <PacketT> ICoderStrategy<PacketT> getParserStrategy(
			@Range(from = 0, to = 127) int strategyIndex) {
		return (ICoderStrategy<PacketT>) parserStrategies[strategyIndex];
	}

	/**
	 * 获取一个包体解析策略
	 *
	 * @param strategyIndex 策略
	 * @return 包体解析策略
	 */
	@SuppressWarnings("unchecked")
	public static <PacketT, DataT> IPacketStrategy<PacketT, DataT> getPacketStrategy(
			@Range(from = 0, to = 127) int strategyIndex) {
		return (IPacketStrategy<PacketT, DataT>) packetStrategies[strategyIndex];
	}

	/**
	 * 注册一个消息解析策略
	 *
	 * @param <PacketT>      包体类型
	 * @param <DataT>>       消息体类型
	 * @param strategyIndex  任务提交者
	 * @param parserStrategy 消息解析策略
	 * @param packetStrategy 包体解析策略
	 */
	public synchronized static <PacketT, DataT> void registerPacketParserStrategy(
			@Range(from = 0, to = 127) int strategyIndex,
			@NotNull ICoderStrategy<PacketT> parserStrategy,
			@NotNull IPacketStrategy<PacketT, DataT> packetStrategy) {
		if (Objects.nonNull(parserStrategies[strategyIndex])) {
			throw new StrategyAlreadyExistsException("parser", strategyIndex);
		}
		if (Objects.nonNull(packetStrategies[strategyIndex])) {
			throw new StrategyAlreadyExistsException("packet", strategyIndex);
		}
		parserStrategies[strategyIndex] = parserStrategy;
		packetStrategies[strategyIndex] = packetStrategy;
	}
}