package com.keimons.platform.player;

import com.keimons.platform.module.IRepeatedPlayerData;
import com.keimons.platform.module.ISingularPlayerData;
import com.keimons.platform.session.ISession;

/**
 * 玩家的接口
 *
 * @param <T> 唯一标识符类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IPlayer<T> extends IPersistence<T> {

	/**
	 * 初始化
	 */
	void init();

	/**
	 * 加载
	 */
	void loaded();

	/**
	 * 登录
	 */
	void online();

	/**
	 * 检查模块是否存在
	 *
	 * @param classes 模块
	 * @return 是否存在
	 */
	@SuppressWarnings("unchecked")
	boolean hasModules(Class<? extends IPlayerData>... classes);

	/**
	 * 从内存中卸载这些模块，被卸载的模块依然存在于数据库中，随时可以加载到内存。
	 *
	 * @param classes 模块
	 */
	@SuppressWarnings("unchecked")
	void unload(Class<? extends IPlayerData>... classes);

	/**
	 * 从内存中卸载不是这些的模块，被卸载的模块依然存在于数据库中，随时可以加载到内存。
	 *
	 * @param classes 模块
	 */
	@SuppressWarnings("unchecked")
	void unloadIfNot(Class<? extends IPlayerData>... classes);

	/**
	 * 增加一个模块数据
	 *
	 * @param data 数据
	 */
	void add(IPlayerData data);

	/**
	 * 获取玩家的一个模块
	 *
	 * @param clazz 模块
	 * @param <V>   模块类型
	 * @return 数据模块
	 */
	<V extends ISingularPlayerData> V get(Class<V> clazz);

	/**
	 * 获取玩家的一个模块
	 *
	 * @param clazz  模块
	 * @param dataId 唯一ID
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @return 模块
	 */
	<K, V extends IRepeatedPlayerData<K>> V get(Class<V> clazz, K dataId);

	/**
	 * 移除玩家的一个数据
	 *
	 * @param clazz  模块
	 * @param dataId 唯一ID
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @return 模块
	 */
	<K, V extends IRepeatedPlayerData<K>> V remove(Class<V> clazz, K dataId);

	/**
	 * 设置是否已加载
	 *
	 * @param loaded true.已加载 false.未加载
	 */
	void setLoaded(boolean loaded);

	/**
	 * 是否已经加载
	 *
	 * @return true.已加载 false.未加载
	 */
	boolean isLoaded();

	/**
	 * 设置session
	 *
	 * @param session 客户端-服务器会话
	 */
	void setSession(ISession session);

	/**
	 * 获取session
	 *
	 * @return 客户端-服务器会话
	 */
	ISession getSession();

	/**
	 * 设置活跃时间
	 *
	 * @param activeTime 活跃时间
	 */
	void setActiveTime(long activeTime);

	/**
	 * 获取活跃时间
	 *
	 * @return 活跃时间
	 */
	long getActiveTime();
}