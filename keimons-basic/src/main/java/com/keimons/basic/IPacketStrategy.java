package com.keimons.basic;

/**
 * 消息解析策略
 *
 * @param <PacketT> 消包体类型
 * @param <DataT>   消息体类型
 */
public interface IPacketStrategy<PacketT, DataT> extends IStrategy {

	/**
	 * 在包体中查找消息号
	 *
	 * @param packet 包体
	 * @return 消息号
	 */
	int findMsgCode(PacketT packet);

	/**
	 * 在包体中查找消息体
	 *
	 * @param packet 包体
	 * @return 消息体
	 */
	DataT findData(PacketT packet);
}