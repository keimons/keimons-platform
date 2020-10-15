package com.keimons.platform.process;

import com.keimons.platform.player.IPlayer;

/**
 * 消息过滤器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IProcessorFilter<PlayerT extends IPlayer<?>, MessageT> {

	/**
	 * 消息过滤器
	 */
	void doFilter(PlayerT player, MessageT message);
}