package com.keimons.platform.process;

import com.keimons.platform.session.ISession;

/**
 * 线程策略
 * <p>
 * 系统允许每个消息使用自己的业务执行器。目前系统提供三种线程策略：
 * <ul>
 *     <li>IO线程业务执行器，这个执行器是由底层通讯框架自带的线程执行器，例如：Netty的{@code worker}线程</li>
 *     <li>线程池业务执行器</li>
 *     <li>线程ID业务执行器</li>
 * </ul>
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IThreadStrategy<SessionT extends ISession, MessageT> {

	/**
	 * 获取执行线程码值
	 * <p>
	 * 线程码用于计算实际执行业务的线程ID，并且，线程码的计算是要优先于业务执行的。标准的流程应该是：
	 * 计算此业务的线程码 -> 根据线程码计算线程ID -> 使用该线程执行这个业务。
	 * 设计的可选参考：
	 * <ul>
	 *     <li>同联盟的业务，由同一个线程执行，这样内部逻辑不需要加锁。</li>
	 *     <li>同一个消息，由同一个线程执行，例如：加入联盟，加入队伍等可能需要对多个目标加锁的业务。</li>
	 * </ul>
	 * 线程码仅作为线程选择的依据。线程码的使用需要配合执行器策略。
	 *
	 * @param session 客户端-服务器会话
	 * @param message 消息体
	 * @return 线程选择会话
	 */
	default int threadCode(SessionT session, MessageT message) {
		return this.hashCode();
	}
}