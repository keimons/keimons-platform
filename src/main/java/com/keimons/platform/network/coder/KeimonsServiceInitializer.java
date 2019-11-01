package com.keimons.platform.network.coder;

import com.keimons.platform.network.KeimonsHandler;
import com.keimons.platform.network.MessageConverter;
import com.keimons.platform.process.ProcessorManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 初始化pipeline
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class KeimonsServiceInitializer<I> extends ChannelInitializer<SocketChannel> {

	private final MessageConverter<I> converter;

	private final ProcessorManager<I> executor;

	public KeimonsServiceInitializer(MessageConverter<I> converter, ProcessorManager<I> processorManager) {
		this.converter = converter;
		this.executor = processorManager;
	}

	@Override
	protected void initChannel(SocketChannel ch) {
		ch.pipeline()
				.addLast("IdleHandler", new IdleStateHandler(5 * 60, 5 * 60, 5 * 60))
				.addLast("FramerDecoder", new ClientRequestFrameDecoder())
				.addLast("RequestDecoder", new ClientRequestDecoder<>(converter.getInboundConverter()))
				.addLast("FramerEncoder", new ServerResponseFrameEncoder())
				.addLast("RequestEncoder", new ServerResponseEncoder<>(converter.getMessageType(), converter.getOutboundConverter()))
				.addLast("KeimonsHandler", new KeimonsHandler<>(converter.getMessageType(), executor));
	}
}
