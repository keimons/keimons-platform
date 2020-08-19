package com.keimons.platform.module;

import com.keimons.platform.iface.ISingularData;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.player.IPlayerData;

/**
 * 单数形式的数据结构
 *
 * @author monkey1993
 * @version 1.0
 **/
public interface ISingularPlayerData extends IPlayerData, ISingularData {

	/**
	 * 初始化（当且仅当对象被创建时调用）
	 * <p>
	 * 由于对象的创建 {@link com.keimons.platform.unit.CodeUtil} 是由反射创建的。所以，
	 * 要对对象的一些内部变量进行初始化工作，对数据进行初始化，仅在对象创建时进行初始化工作，当数
	 * 据已经被初始化后，不会对数据进行第二次初始化。
	 *
	 * @param player 玩家
	 * @param <T>    玩家类型
	 */
	<T extends IPlayer<?>> void init(T player);
}