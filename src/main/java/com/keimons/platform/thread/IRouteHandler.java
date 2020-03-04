package com.keimons.platform.thread;

import com.keimons.platform.process.IHandler;
import com.keimons.platform.session.ISession;

/**
 * 自定义的路有规则
 * <p>
 * 系统允许开发人员自定义路由规则，路由规则是整个线程模型中最核心的点。通过用户自定义的路由
 * 规则，将不同的协议交由不同的线程进行处理。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IRouteHandler<T extends ISession, O> extends IHandler<T, O> {

	/**
	 * 获取执行时间
	 *
	 * @return 执行时间
	 */
	int getExecuteTime();

	/**
	 * 协议路由规则
	 * <p>
	 * 允许用户自定义该逻辑由哪个线程处理，通过一定的路由规则，可以指定该业务由哪个特定的线程
	 * 处理。例如：通过公会ID将该公会的所有消息路由到指定的线程处理，这样，该公会的操作都会变
	 * 成单线程的操作。
	 *
	 * @param session  会话
	 * @param packet   消息体
	 * @param maxIndex 最大下标
	 * @return 线程index
	 */
	default int route(T session, O packet, int maxIndex) {
		return session.getSessionId() % maxIndex;
	}
}