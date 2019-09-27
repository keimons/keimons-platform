package com.keimons.platform.iface;

import com.keimons.platform.annotation.AModule;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.player.ModuleUtil;

/**
 * 玩家数据模块
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
@AModule
public interface IPlayerData extends IData, ILoaded {

	/**
	 * 初始化(当且仅当对象被创建时调用)
	 * <p>
	 * 由于对象的创建 {@link ModuleUtil} 是由反射创建的
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
	String getModuleName();

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

	/**
	 * 获取当前的玩家数据版本
	 * <p>
	 * 这是设计中的一个核心接口，依赖于模块的版本号，决定模块是否需要升级
	 *
	 * @return 当前的玩家数据版本
	 */
	int getVersion();
}