package com.keimons.nutshell.module;

import com.keimons.nutshell.player.IPlayer;
import com.keimons.nutshell.player.IPlayerData;
import com.keimons.nutshell.unit.JProtobufUtil;

/**
 * 单数形式的数据结构
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface ISingularPlayerData extends IPlayerData, ISingularData {

	/**
	 * 初始化（当且仅当对象被创建时调用）
	 * <p>
	 * 由于对象的创建 {@link JProtobufUtil} 是由反射创建的。所以，
	 * 要对对象的一些内部变量进行初始化工作，对数据进行初始化，仅在对象创建时进行初始化工作，当数
	 * 据已经被初始化后，不会对数据进行第二次初始化。
	 *
	 * @param player 玩家
	 * @param <T>    玩家类型
	 */
	<T extends IPlayer<?>> void init(T player);
}