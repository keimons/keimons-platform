package com.keimons.platform.network.process;

import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;

/**
 * 消息处理器
 */
public interface IProcessor {

	/**
	 * 处理消息 玩家对中心服务器
	 *
	 * @param session 会话
	 * @param packet  客户端发送过来的数据
	 */
	void processor(Session session, Packet packet);
}