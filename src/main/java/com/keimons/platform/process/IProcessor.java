package com.keimons.platform.process;

import com.keimons.platform.executor.CommitterManager;
import com.keimons.platform.executor.ExecutorManager;
import com.keimons.platform.handler.IHandler;
import com.keimons.platform.handler.ICoderStrategy;
import com.keimons.platform.session.ISession;

/**
 * 消息处理器
 * <p>
 * 我们会将消息包装成一个任务，然后，通过{@link CommitterManager}任务提交策略和{@link ExecutorManager}任务
 * 执行策略来执行一个任务。这个任务可能是由任务提交线程直接处理，也有可能是将任务提交到线程池中。
 * <p>
 * 注意：在运行时，需要获取{@code MessageT}的运行时类型。
 *
 * @author monkey1993
 * @version 1.0
 * @see AProcessor 消息描述
 * @see IHandler 任务处理
 * @see ICoderStrategy 包体解析策略
 * @see CommitterManager 任务提交器
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