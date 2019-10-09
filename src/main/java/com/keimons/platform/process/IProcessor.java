package com.keimons.platform.process;

import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;

/**
 * 消息处理器
 * <p>
 * 在这个接口中，仅仅设计了核心的接口，并设计例如getMsgCode() getInterval() getDesc()等接口
 * 考虑到整个项目都会使用{注解-安装}的模式，所以，将描述信息存放在了
 * {@link com.keimons.platform.annotation.AProcessor}而不
 * 是在这个接口中实现
 * <p>
 * 在设计模式中，这个接口的作用，也仅仅是用来处理业务逻辑的，不另做其他用途
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IProcessor {

	/**
	 * 处理消息
	 * <p>
	 * 当服务器接收并解码消息后，交由对应的消息处理器处理
	 *
	 * @param session 客户端-服务器 会话
	 * @param packet  客户端发送过来的数据
	 */
	void processor(Session session, Packet packet);
}