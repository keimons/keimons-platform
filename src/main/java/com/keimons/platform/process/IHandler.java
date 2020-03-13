package com.keimons.platform.process;

import com.keimons.platform.executor.IExecutorEnum;
import com.keimons.platform.session.ISession;

/**
 * 消息信息
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IHandler<MessageT extends ISession, SessionT> {

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
     * 获取线程分配方法
     *
     * @return 分配的线程 如果返回值为{@code null}，则线程不会被指定线程
     */
    SelectionThreadFunction<MessageT, SessionT> getRule();

    /**
     * 构建一个任务
     *
     * @param session 客户端服务器会话
     * @param message 要处理的消息体
     * @return 任务
     */
    Runnable createTask(MessageT session, SessionT message);

    /**
     * 获取线程池类型
     *
     * @return 消息处理线程池类型
     */
    Enum<? extends IExecutorEnum> getExecutorType();
}