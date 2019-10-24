package com.keimons.platform;

import com.keimons.platform.log.LogService;
import com.keimons.platform.network.ICoder;
import com.keimons.platform.network.coder.KeimonsServiceInitializer;
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
public class KeimonsTcpNet<T> {

	/**
	 * 解码器
	 */
	private static ICoder<byte[], ?> decode;

	/**
	 * 编码器
	 */
	private static ICoder<?, byte[]> encode;

	private static EventLoopGroup bossGroup;
	private static EventLoopGroup workerGroup;

	private Class<T> carrier;

	@SuppressWarnings("unchecked")
	public static <T> ICoder<byte[], T> DECODE() {
		return (ICoder<byte[], T>) decode;
	}

	@SuppressWarnings("unchecked")
	public static <T> ICoder<T, byte[]> ENCODE() {
		return (ICoder<T, byte[]>) encode;
	}

	private KeimonsTcpNet(Class<T> carrier, ICoder<byte[], T> decode, ICoder<T, byte[]> encode) {
		KeimonsTcpNet.decode = decode;
		KeimonsTcpNet.encode = encode;
		this.carrier = carrier;
	}

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
			b.childHandler(new KeimonsServiceInitializer(carrier));
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.SO_REUSEADDR, true);
			b.childOption(ChannelOption.TCP_NODELAY, true); // 关闭Nagle的算法
			b.childOption(ChannelOption.SO_RCVBUF, 64 * 1024);
			b.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
			b.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(64 * 1024, 10 * 1024 * 1024));
			ChannelFuture channelFuture = b.bind(8080).addListener((ChannelFutureListener) future -> System.out.println("服务监听端口：" + 8080)).sync();

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
	public static void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	/**
	 * 初始化通讯模块
	 *
	 * @param carrier 数据载体泛型
	 * @param decode  解码方式
	 * @param encode  编码方式
	 * @param <T>     输入/输出类型
	 */
	public static <T> void init(Class<T> carrier, ICoder<byte[], T> decode, ICoder<T, byte[]> encode) {
		KeimonsTcpNet<T> net = new KeimonsTcpNet<>(carrier, decode, encode);
		Thread thread = new Thread(net::start, "TCP-SERVER");
		thread.start();
	}
}