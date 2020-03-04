package com.keimons.platform.player;

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
	 * 获取用于标识唯一玩家的数据唯一标识符
	 * <p>
	 * 将玩家数据进行持久化时，需要依赖这个这个唯一数据标识符号，这个唯一标识符是不能重复的。
	 * 将数据存储至数据库时，uuid即为标识唯一用户的主键，根据主键对玩家进行索引查询。注意，合
	 * 服后需要依然能够准追找到这个玩家。
	 *
	 * @return 唯一标识符
	 */
	T getIdentifier();

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
	 * 移除不是这些的模块
	 *
	 * @param classes 模块
	 */
	@SuppressWarnings("unchecked")
	void clearIfNot(Class<? extends IPlayerData>... classes);

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
	 * @param <V>    模块类型
	 * @return 模块
	 */
	<V extends IRepeatedPlayerData<?>> V get(Class<V> clazz, Object dataId);

	/**
	 * 移除玩家的一个数据
	 *
	 * @param clazz  模块
	 * @param dataId 唯一ID
	 * @param <V>    模块类型
	 * @return 模块
	 */
	<V extends IRepeatedPlayerData<?>> V remove(Class<V> clazz, Object dataId);

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