package com.keimons.platform.executor;

import com.keimons.platform.session.ISession;

/**
 * 执行器策略
 *
 * @param <SessionT> 会话
 */
public interface IExecutorStrategy<SessionT extends ISession, MessageT> {

	void execute(SessionT session, MessageT message);
}