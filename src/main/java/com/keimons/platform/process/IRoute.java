package com.keimons.platform.process;

import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;

/**
 * 路由规则
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IRoute {

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
	default int route(Session session, Packet packet, int maxIndex) {
		return session.getSessionId() % maxIndex;
	}
}