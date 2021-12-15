package com.keimons.nutshell.network.coder;

import com.keimons.nutshell.network.KeimonsHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 初始化pipeline
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class KeimonsServiceInitializer<I> extends ChannelInitializer<SocketChannel> {

	private final CodecAdapter<I> codecAdapter;

	private final ByteAdapter byteAdapter;

	public KeimonsServiceInitializer(CodecAdapter<I> converter, ByteAdapter byteAdapter) {
		this.codecAdapter = converter;
		this.byteAdapter = byteAdapter;
	}

	@Override
	protected void initChannel(SocketChannel ch) {
		ch.pipeline()
				.addLast("IdleHandler", new IdleStateHandler(5 * 60, 5 * 60, 5 * 60))
				.addLast("byteAdapter", byteAdapter)
				.addLast("codecAdapter", codecAdapter)
				.addLast("KeimonsHandler", new KeimonsHandler<>(codecAdapter.getMessageType()));
	}
}