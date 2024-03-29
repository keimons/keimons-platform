package com.keimons.nutshell.session;

import com.keimons.nutshell.executor.LocaleCommitterPolicy;
import com.keimons.nutshell.executor.NoneExecutorPolicy;
import com.keimons.nutshell.player.IPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * 会话接口
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public interface ISession {

	/**
	 * 获取任务队列的Key
	 * <p>
	 * 可以参考的设计：每个玩家应该拥有一个属于自己的任务队列。如果这个{@code session}还没有绑定玩家，
	 * 则可以指定该消息使用{@link LocaleCommitterPolicy}排队策略。使用{@link LocaleCommitterPolicy#DEFAULT}默认的
	 * 任务队列。如果使用默认任务队列，则不建议使用{@link NoneExecutorPolicy}。
	 */
	@NotNull Object getExecutorCode();

	/**
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 获取这个session对应的玩家
	 *
	 * @return 玩家
	 */
	IPlayer<?> getPlayer();

	/**
	 * 设置这个Session对应的玩家
	 *
	 * @param player 玩家
	 */
	void setPlayer(IPlayer<?> player);

	/**
	 * 获取Session唯一Id
	 *
	 * @return Session唯一Id
	 */
	int getSessionId();

	/**
	 * 获取Session的上次活跃时间
	 *
	 * @return 上次活跃时间
	 */
	long getLastActiveTime();
}