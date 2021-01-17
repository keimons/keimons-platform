package com.keimons.platform.handler;

import jdk.internal.vm.annotation.ForceInline;

/**
 * 消息体解析策略
 * <p>
 * 在网络层中传递的消息是二进制流，当服务器接收到消息后，会对消息进行二次封装。经过二次封装后的消息，
 * 就是真正的消息体了。此策略负责将消息体的形式由{@code byte[]}解析为应用层定义消息载体。例如，常
 * 用的消息载体有：json、protobuf等。
 * <p>
 * 消息处理流程：
 * <p>
 * +--------+       +--------+       +---------+
 * | byte[] | ----> | packet | ----> | message |
 * +--------+       +--------+       +---------+
 * <p>
 * 消息{@code byte[]}的来源可能是Netty、Mina等网络框架。
 *
 * @param <PacketT> 包体
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IParserStrategy<PacketT> {

	/**
	 * 包体解析
	 * <p>
	 * 将消息解析成指定的消息类型，例如：json、protobuf等。
	 *
	 * @param packet 已经经过了二次封装的完整消息体
	 * @return 指定载体类型的包体
	 */
	@ForceInline
	PacketT parsePacket(byte[] packet) throws Exception;
}