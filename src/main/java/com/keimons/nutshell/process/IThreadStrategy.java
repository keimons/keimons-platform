package com.keimons.nutshell.process;

import com.keimons.nutshell.executor.CodeExecutorPolicy;
import com.keimons.nutshell.executor.IExecutorStrategy;
import com.keimons.nutshell.executor.NoneExecutorPolicy;
import com.keimons.nutshell.executor.PoolExecutorPolicy;
import com.keimons.nutshell.session.ISession;

/**
 * 线程码策略
 * <p>
 * 系统允许每个消息使用自己的任务执行策略。系统默认提供三种策略：
 * <ul>
 *     <li>{@link NoneExecutorPolicy}无操作任务执行策略，直接由调用线程来执行这个任务。</li>
 *     <li>{@link PoolExecutorPolicy}线程池任务执行策略，任务提交至线程池中执行。</li>
 *     <li>{@link CodeExecutorPolicy}线程码任务执行策略，根据任务提供的线程码，选择线程执行任务。</li>
 * </ul>
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IThreadStrategy<SessionT extends ISession, MessageT> {

	/**
	 * 获取执行线程码值
	 * <p>
	 * 线程码用于计算实际执行任务的线程码，并且，线程码的计算是要优先于任务执行的。标准的流程应该是：
	 * 计算此任务的线程码 -> 根据线程码计算线程码 -> 使用该线程执行这个任务。
	 * 设计的可选参考：
	 * <ul>
	 *     <li>同联盟的消息，由同一个线程执行，这样内部逻辑不需要加锁。</li>
	 *     <li>同一个消息，由同一个线程执行，例如：加入联盟，加入队伍等可能需要对多个目标加锁的操作。</li>
	 * </ul>
	 * 线程码仅作为线程选择的依据。线程码的使用需要配合{@link IExecutorStrategy}任务执行策略。
	 *
	 * @param session 客户端-服务器会话
	 * @param message 消息体
	 * @return 线程选择会话
	 */
	default int threadCode(SessionT session, MessageT message) {
		return 0;
	}
}