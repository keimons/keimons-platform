package com.keimons.platform.process;

/**
 * 分配线程接口式函数
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@FunctionalInterface
public interface SelectionThreadFunction<SessionT, MessageT> {

	/**
	 * 获取被分配到的线程
	 *
	 * @param session  会话
	 * @param message  消息体
	 * @param maxIndex 最大值
	 * @return 分配的线程
	 */
	int selection(SessionT session, MessageT message, int maxIndex);
}