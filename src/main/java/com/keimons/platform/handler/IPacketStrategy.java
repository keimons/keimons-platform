package com.keimons.platform.handler;

import jdk.internal.vm.annotation.ForceInline;

/**
 * 消息解析策略
 *
 * @param <PacketT> 消包体类型
 * @param <DataT>   消息体类型
 */
public interface IPacketStrategy<PacketT, DataT> {

	/**
	 * 在包体中查找消息号
	 *
	 * @param packet 包体
	 * @return 消息号
	 */
	@ForceInline
	int findMsgCode(PacketT packet);

	/**
	 * 在包体中查找消息体
	 *
	 * @param packet 包体
	 * @return 消息体
	 */
	@ForceInline
	DataT findData(PacketT packet);
}