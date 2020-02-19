package com.keimons.platform.process;

import com.keimons.platform.session.Session;
import com.keimons.platform.thread.DefaultExecutorConfig;
import com.keimons.platform.thread.IExecutorConfig;
import com.keimons.platform.thread.IThreadRoute;
import com.keimons.platform.thread.KeimonsExecutor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息处理器
 * <p>
 * 在这个接口中，仅仅设计了核心的接口，并设计例如getMsgCode() getInterval() getDesc()等接口
 * 考虑到整个项目都会使用{注解-安装}的模式，所以，将协议的描述信息存放在了
 * {@link AProcessor}而不
 * 是在这个接口中实现
 * <p>
 * 在设计模式中，这个接口的作用，也仅仅是用来处理业务逻辑的，不另做其他用途
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseProcessor<T> implements IHandler<T> {

	private static final KeimonsExecutor EXECUTOR = new KeimonsExecutor(DefaultExecutorConfig.class);

	/**
	 * 消息号
	 */
	protected final int msgCode;

	/**
	 * 线程池类型
	 */
	protected final Enum<? extends IExecutorConfig> executorConfig;

	/**
	 * 请求间隔
	 */
	protected final int interval;

	/**
	 * 消息号描述
	 */
	protected final String desc;

	/**
	 * 采样频率
	 */
	protected final int sampling;

	/**
	 * 总的执行时间
	 */
	private int executorTime;

	/**
	 * 采样频率
	 */
	private int[] executorTimes;

	/**
	 * 写入位置
	 */
	private AtomicInteger index = new AtomicInteger(0);

	/**
	 * 与数字，通过计算 index & AND 计算出来下标位置
	 */
	private int AND;

	protected BaseProcessor() {
		AProcessor annotation = this.getClass().getAnnotation(AProcessor.class);
		this.msgCode = annotation.MsgCode();
		this.interval = annotation.Interval();
		this.desc = annotation.Desc();
		this.sampling = annotation.Sampling();
		this.executorConfig = annotation.ExecutorConfig();

		this.executorTimes = new int[this.sampling];
		this.AND = this.sampling - 1;
	}

	/**
	 * 选择线程等级
	 * <p>
	 * 如果线程是自适应等级，则根据历史本消息执行时长，计算出来它应该使用的线程。
	 */
	@Override
	public boolean handler(Session session, T packet) {
		DefaultExecutorConfig config = (DefaultExecutorConfig) this.executorConfig;
		if (config == DefaultExecutorConfig.AUTO) {
			int executeTime = executorTime / sampling;
			if (executeTime < 20) {
				config = DefaultExecutorConfig.FAST;
			} else {
				config = DefaultExecutorConfig.SLOW;
			}
		}

		Runnable runnable = () -> {
			try {
				processor(session, packet);
			} finally {
				session.finish();
			}
		};
		if (config.isRoute()) {
			int route = route(session, packet, config.getThreadNumb());
			EXECUTOR.execute(config, route, runnable);
		} else {
			EXECUTOR.execute(config, runnable);
		}
		return true;
	}

	/**
	 * 获取旅游线程
	 *
	 * @param session  会话
	 * @param packet   消息体
	 * @param maxIndex 最大线程ID
	 * @return 线程index
	 */
	public int route(Session session, Object packet, int maxIndex) {
		if (this instanceof IThreadRoute) {
			return ((IThreadRoute) this).route(session, packet, maxIndex);
		} else {
			return session.getSessionId() % maxIndex;
		}
	}

	/**
	 * 更新消息的执行时长
	 *
	 * @param executeTime 消息执行时长
	 */
	public void updateExecuteTime(int executeTime) {
		DefaultExecutorConfig config = (DefaultExecutorConfig) this.executorConfig;
		if (config == DefaultExecutorConfig.AUTO) {
			int index = this.index.getAndIncrement() & AND;
			executorTime -= this.executorTimes[index];
			this.executorTimes[index] = executeTime;
			executorTime += executeTime;
		}
	}

	/**
	 * 处理消息
	 * <p>
	 * 当服务器接收并解码消息后，交由对应的消息处理器处理
	 *
	 * @param session 客户端-服务器 会话
	 * @param packet  客户端发送过来的数据
	 */
	public abstract void processor(Session session, T packet);

	@Override
	public Enum<? extends IExecutorConfig> getExecutorConfig() {
		return executorConfig;
	}
}