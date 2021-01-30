package com.keimons.platform.network;

import com.keimons.platform.log.ILogger;
import com.keimons.platform.log.LoggerFactory;

/**
 * 会话工厂
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-29
 * @since 1.8
 **/
public abstract class SessionFactory {

	private static ILogger logger = LoggerFactory.getLogger(SessionFactory.class);

	/**
	 * 会话工厂
	 */
	private static SessionFactory factory;

	/**
	 * 创建一个连接
	 *
	 * @param connect 原始连接
	 * @param <T>     会话
	 * @return 新连接
	 */
	public static <ConnectT, T extends ISession<ConnectT>> T create(ConnectT connect) {
		if (factory == null) {
			try {
				factory = NettySessionFactory.INSTANCE;
				logger.debug("Using ChannelHandlerContext as the default session");
			} catch (Throwable ignore) {
				factory = MinaSessionFactory.INSTANCE;
				logger.debug("Using IoSession as the default session");
			}
		}
		return factory.create0(connect);
	}

	/**
	 * 获取日志文件
	 *
	 * @param connect 日志名称
	 * @return 日志文件
	 */
	protected abstract <ConnectT, T extends ISession<ConnectT>> T create0(ConnectT connect);

	/**
	 * 依赖于Slf4J的日志工厂
	 */
	static class NettySessionFactory extends SessionFactory {

		static final NettySessionFactory INSTANCE = new NettySessionFactory();

		@Override
		@SuppressWarnings("unchecked")
		protected <ConnectT, T extends ISession<ConnectT>> T create0(ConnectT connect) {
			return (T) new NettySession();
		}
	}

	/**
	 * 依赖于Jdk的日志工厂
	 */
	static class MinaSessionFactory extends SessionFactory {

		static final MinaSessionFactory INSTANCE = new MinaSessionFactory();

		@Override
		protected <ConnectT, T extends ISession<ConnectT>> T create0(ConnectT connect) {
			return (T) new MinaSession();
		}
	}
}