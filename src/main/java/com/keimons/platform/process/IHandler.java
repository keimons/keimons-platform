package com.keimons.platform.process;

import com.keimons.platform.executor.IExecutorStrategy;
import com.keimons.platform.session.ISession;

/**
 * 消息信息
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IHandler<SessionT extends ISession, DataT, MessageT> {

	/**
	 * 获取消息号
	 *
	 * @return 消息号
	 */
	int getMsgCode();

	/**
	 * 获取消息描述
	 *
	 * @return 消息描述
	 */
	String getDesc();

	/**
	 * 获取消息处理间隔
	 *
	 * @return 消息处理间隔
	 */
	int getInterval();

	/**
	 * 获取消息处理器
	 *
	 * @return 消息处理器
	 */
	IProcessor<SessionT, MessageT> getProcessor();

	/**
	 * 获取消息类型
	 *
	 * @return 包体类型
	 */
	Class<DataT> getDataType();

	/**
	 * 获取消息体类型
	 *
	 * @return 消息类
	 */
	Class<MessageT> getMessageType();

	/**
	 * 解析消息体
	 *
	 * @param packet 包体
	 * @return {@code null}表示没有消息体
	 * @throws Exception 消息解析异常
	 */
	MessageT parseMessage(DataT packet) throws Exception;

	/**
	 * 返回业务执行器策略
	 *
	 * @return 业务执行策略 {@code null}表示不使用执行器策略直接处理
	 */
	default IExecutorStrategy<SessionT, MessageT> getExecutorStrategy() {
		return null;
	}
}