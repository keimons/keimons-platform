package com.keimons.platform.handler;

import jdk.internal.vm.annotation.ForceInline;

/**
 * 消息体解析策略
 *
 * @param <PacketT> 包体
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IParserStrategy<PacketT> {

	/**
	 * 包体解析
	 *
	 * @param packet 消息体
	 * @return 包体
	 */
	@ForceInline
	PacketT parsePacket(byte[] packet) throws Exception;
}