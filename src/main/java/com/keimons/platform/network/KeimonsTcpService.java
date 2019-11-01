package com.keimons.platform.network;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.coder.KeimonsServiceInitializer;
import com.keimons.platform.process.ProcessorManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TCP通讯模块
 * <p>
 * 底层采用Netty实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class KeimonsTcpService<T> {

	/**
	 * BossGroup线程池
	 */
	private EventLoopGroup bossGroup;

	/**
	 * 业务逻辑线程池
	 */
	private EventLoopGroup workerGroup;

	private final MessageConverter<T> converter;

	private final ProcessorManager<T> executor;

	public KeimonsTcpService(MessageConverter<T> converter, ProcessorManager<T> executor) {
		this.converter = converter;
		this.executor = executor;
	}

	/**
	 * 启动网络层
	 */
	private void start() {
		String osName = System.getProperty("os.name");
		if (osName.contains("Linux")) {
			bossGroup = new EpollEventLoopGroup();
			workerGroup = new EpollEventLoopGroup();
		} else {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup(KeimonsServer.KeimonsConfig.getNetThreadCount()[0]);
		}
		try {
			ServerBootstrap b = new ServerBootstrap();
			if (osName.contains("Linux")) {
				b.channel(EpollServerSocketChannel.class);
			} else {
				b.channel(NioServerSocketChannel.class);
			}
			b.group(bossGroup, workerGroup);
			b.childHandler(new KeimonsServiceInitializer<>(converter, executor));
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.childOption(ChannelOption.TCP_NODELAY, true); // 关闭Nagle的算法
			b.childOption(ChannelOption.SO_RCVBUF, 64 * 1024);
			b.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
			b.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(64 * 1024, 10 * 1024 * 1024));
			ChannelFuture channelFuture = b.bind(KeimonsServer.KeimonsConfig.getPort()).addListener((ChannelFutureListener) future -> System.out.println("服务监听端口：" + 8080)).sync();

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			LogService.error(e);
			System.exit(-1);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * 关闭Netty的线程池
	 */
	public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * 初始化通讯模块
	 */
	public void init() {
		Thread thread = new Thread(this::start, "TCP-SERVER");
		thread.start();
	}
}