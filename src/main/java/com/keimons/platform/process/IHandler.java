package com.keimons.platform.process;

import com.keimons.platform.session.ISession;
import com.keimons.platform.executor.IExecutorEnum;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-03-05
 * @since 1.8
 **/
public interface IHandler<T extends ISession, O> {

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
     * 构建一个任务
     *
     * @param session 客户端服务器会话
     * @param message 要处理的消息体
     * @return 任务
     */
    Runnable createTask(T session, O message);

    /**
     * 获取线程池类型
     *
     * @return 消息处理线程池类型
     */
    Enum<? extends IExecutorEnum> getExecutorType();
}