package com.keimons.platform.iface;

import com.keimons.platform.player.AbsPlayer;

/**
 * 玩家数据
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
public interface IPlayerData extends IData, ILoaded {

	/**
	 * 初始化(当且仅当对象被创建时调用)
	 * <p>
	 * 由于对象的创建 {@link com.keimons.platform.player.DataUtil} 是由反射创建的
	 * 所以，要对对象的一些内部变量进行初始化工作
	 * 对数据进行初始化，仅在对象创建时进行初始化工作
	 * 当数据已经被初始化后，禁止进行第二次初始化
	 *
	 * @param player 玩家
	 */
	void init(AbsPlayer player);

	/**
	 * 模块类型
	 *
	 * @return 这个模块的类型
	 */
	Enum<? extends IModule> getModuleType();

	/**
	 * 上次计算的MD5
	 * <p>
	 * 通过比较两次计算得出的MD5值，决定这个模块数据是否需要存储
	 *
	 * @return 上次计算的MD5可能为空
	 */
	String getLastMd5();

	/**
	 * 设置上次计算的MD5
	 *
	 * @param lastMd5 模块的MD5值
	 */
	void setLastMd5(String lastMd5);
}