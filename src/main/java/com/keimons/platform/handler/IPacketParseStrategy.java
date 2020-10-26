package com.keimons.platform.handler;

/**
 * 消息体解析策略
 *
 * @param <DataT>   消息
 * @param <PacketT> 包体
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IPacketParseStrategy<PacketT, DataT> {

	/**
	 * 包体解析
	 *
	 * @param packet 消息体
	 * @return 包体
	 */
	PacketT parsePacket(byte[] packet) throws Exception;

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