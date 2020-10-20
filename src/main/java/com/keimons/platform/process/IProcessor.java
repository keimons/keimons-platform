package com.keimons.platform.process;

import com.keimons.platform.executor.ExecutorManager;
import com.keimons.platform.session.ISession;

/**
 * 消息处理器
 * <p>
 * 当服务器接受到来自客户端的消息后，交给对应的消息处理器处理。在这个框架中，
 * 消息处理的逻辑是线程池中运行的。
 * <p>
 * 注意：这个类必须是抽象的，否则不能获取到{@code MessageT}的类型。
 *
 * @author monkey1993
 * @version 1.0
 * @see ExecutorManager 任务执行器
 * @since 1.8
 */
public interface IProcessor<SessionT extends ISession, MessageT>
		extends IThreadStrategy<SessionT, MessageT> {

	/**
	 * 消息处理逻辑
	 *
	 * @param session 客户端-服务器 会话
	 * @param message 消息体
	 */
	void processor(SessionT session, MessageT message);
}