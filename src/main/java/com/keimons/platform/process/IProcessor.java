package com.keimons.platform.process;

import com.keimons.platform.session.ISession;
import com.keimons.platform.executor.KeimonsExecutor;

/**
 * 消息处理器
 * <p>
 * 当服务器接受到来自客户端的消息后，交给对应的消息处理器处理。在这个框架中，
 * 消息处理的逻辑是线程池中运行的。
 *
 * @author monkey1993
 * @version 1.0
 * @see KeimonsExecutor 业务处理线程池
 * @since 1.8
 */
public interface IProcessor<SessionT extends ISession, MessageT> {

	/**
	 * 消息处理逻辑
	 *
	 * @param session 客户端-服务器 会话
	 * @param packet  消息体
	 */
	void processor(SessionT session, MessageT packet);
}