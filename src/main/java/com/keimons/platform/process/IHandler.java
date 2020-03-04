package com.keimons.platform.process;

import com.keimons.platform.session.ISession;
import com.keimons.platform.thread.IExecutorEnum;

/**
 * 消息处理器
 * <p>
 * 当服务器接受到来自客户端的消息后，交给对应的消息处理器处理
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IHandler<T extends ISession, O> {

	/**
	 * 获取执行的线程
	 *
	 * @return 执行线程
	 */
	Enum<? extends IExecutorEnum> getExecutor();

	/**
	 * 消息处理逻辑
	 *
	 * @param session 客户端-服务器 会话
	 * @param packet  消息体
	 */
	void handler(T session, O packet);
}