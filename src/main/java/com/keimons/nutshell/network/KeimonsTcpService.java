package com.keimons.nutshell.network;

import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;
import com.keimons.nutshell.network.coder.DefaultByteAdapter;
import com.keimons.nutshell.network.coder.KeimonsServiceInitializer;
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
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class KeimonsTcpService implements NetService {

	private static final ILogger logger = LoggerFactory.getLogger(KeimonsTcpService.class);

	/**
	 * BossGroup线程池
	 */
	private EventLoopGroup bossGroup;

	/**
	 * 业务逻辑线程池
	 */
	private EventLoopGroup workerGroup;

	@Override
	public void start(int port) {
		String osName = System.getProperty("os.name");
		if (osName.contains("Linux")) {
			bossGroup = new EpollEventLoopGroup();
			workerGroup = new EpollEventLoopGroup();
		} else {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup(8);
		}
		try {
			ServerBootstrap b = new ServerBootstrap();
			if (osName.contains("Linux")) {
				b.channel(EpollServerSocketChannel.class);
			} else {
				b.channel(NioServerSocketChannel.class);
			}
			b.group(bossGroup, workerGroup);
			b.childHandler(new KeimonsServiceInitializer<>(null, new DefaultByteAdapter()));
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.childOption(ChannelOption.TCP_NODELAY, true); // 关闭Nagle的算法
			b.childOption(ChannelOption.SO_RCVBUF, 64 * 1024);
			b.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
			b.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(64 * 1024, 10 * 1024 * 1024));
			ChannelFuture channelFuture = b.bind(port).addListener((ChannelFutureListener) future -> System.out.println("服务监听端口：" + 8080)).sync();

			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error(e);
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
		Thread thread = new Thread(() -> start(-1), "TCP-SERVER");
		thread.setName("NetService");
		thread.start();
	}
}