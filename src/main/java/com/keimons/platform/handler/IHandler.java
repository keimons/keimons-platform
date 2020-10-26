package com.keimons.platform.handler;

import com.keimons.platform.executor.ExecutorManager;
import com.keimons.platform.executor.IExecutorStrategy;
import com.keimons.platform.executor.NoneExecutorPolicy;
import com.keimons.platform.process.IProcessor;
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
	 * 任务提交策略
	 *
	 * @return 任务提交策略
	 */
	int getCommitterStrategy();

	/**
	 * 任务执行策略
	 * <p>
	 * 如果没有指定策略，则使用{@link NoneExecutorPolicy}无操作任务执行策略。如果指定某一个自定义的任务执行器，
	 * 需要先注册{@link ExecutorManager#registerExecutorStrategy(int, IExecutorStrategy)}这个任务执行
	 * 器，这样才能被任务执行器所使用。
	 *
	 * @return 策略
	 * @see ExecutorManager 执行器管理
	 */
	int getExecutorStrategy();

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
}