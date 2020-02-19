package com.keimons.platform.process;

import com.keimons.platform.session.Session;
import com.keimons.platform.thread.IExecutorConfig;

/**
 * 消息号的信息
 * <p>
 * 系统中的消息号会包装上消息号的信息
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IHandler<T> {

	/**
	 * 获取线程池类型
	 *
	 * @return 线程池类型
	 */
	Enum<? extends IExecutorConfig> getExecutorConfig();

	/**
	 * 选择执行的线程
	 */
	boolean handler(Session session, T packet);
}