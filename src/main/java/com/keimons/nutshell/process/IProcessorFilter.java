package com.keimons.nutshell.process;

import com.keimons.nutshell.player.IPlayer;

/**
 * 消息过滤器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IProcessorFilter<PlayerT extends IPlayer<?>, MessageT> {

	/**
	 * 消息过滤器
	 */
	void doFilter(PlayerT player, MessageT message);
}