package com.keimons.nutshell.network;

import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import org.apache.mina.core.session.IoSession;

import java.util.Arrays;

/**
 * 连接工厂
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class SessionFactory {

	private static final ILogger logger = LoggerFactory.getLogger(SessionFactory.class);

	/**
	 * 连接工厂
	 */
	private static SessionFactory factory;

	/**
	 * 创建一个连接
	 *
	 * @param connect 原始连接
	 * @param <T>     会话
	 * @return 新连接
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ISession> T create(Object connect) {
		if (factory == null) {
			try {
				factory = NettySessionFactory.INSTANCE;
				logger.debug("Using ChannelHandlerContext as the default session");
			} catch (Throwable ignore) {
				factory = MinaSessionFactory.INSTANCE;
				logger.debug("Using IoSession as the default session");
			}
		}
		return (T) factory.create0(connect);
	}

	/**
	 * 获取日志文件
	 *
	 * @param connect 日志名称
	 * @return 日志文件
	 */
	protected abstract ISession create0(Object connect);

	/**
	 * 依赖于Slf4J的日志工厂
	 */
	static class NettySessionFactory extends SessionFactory {

		static final NettySessionFactory INSTANCE = new NettySessionFactory();

		@Override
		protected ISession create0(Object connect) {
			return new NettySession((ChannelHandlerContext) connect);
		}
	}

	/**
	 * 依赖于Jdk的日志工厂
	 */
	static class MinaSessionFactory extends SessionFactory {

		static final MinaSessionFactory INSTANCE = new MinaSessionFactory();

		@Override
		protected ISession create0(Object connect) {
			return new MinaSession((IoSession) connect);
		}
	}

	public static void main(String[] args) {
		ISession session = SessionFactory.create(null);
		byte[] bytes = new byte[4];
		bytes[0] = 0;
		bytes[1] = 1;
		bytes[2] = 2;
		bytes[3] = 3;
		session.write(bytes).addListener((IWriteFutureListener) future -> {
			System.out.println(Arrays.toString(bytes));
			System.out.println(future.getFuture().toString());
		});
	}
}