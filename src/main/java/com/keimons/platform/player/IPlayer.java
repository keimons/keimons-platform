package com.keimons.platform.player;

import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.module.BaseModules;
import com.keimons.platform.session.Session;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 玩家的接口
 *
 * @param <T> 唯一标识符类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IPlayer<T> {

	/**
	 * 获取用于标识唯一玩家的数据唯一标识符
	 * <p>
	 * 将玩家数据进行持久化时，需要依赖这个这个唯一数据标识符号，这个唯一标识符是不能重复的。
	 * 将数据存储至数据库时，uuid即为标识唯一用户的主键，根据主键对玩家进行索引查询。注意，合
	 * 服后需要依然能够准追找到这个玩家。
	 *
	 * @return 唯一标识符
	 */
	T uuid();

	/**
	 * 设置session
	 *
	 * @param session 客户端-服务器会话
	 */
	void setSession(Session session);

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
	<V extends IRepeatedPlayerData> V get(Class<V> clazz, Object dataId);

	/**
	 * 移除玩家的一个数据
	 *
	 * @param clazz  模块
	 * @param dataId 唯一ID
	 * @param <V>    模块类型
	 * @return 模块
	 */
	<V extends IRepeatedPlayerData> V remove(Class<V> clazz, Object dataId);

	/**
	 * 获取玩家所有的模块数据
	 *
	 * @return 玩家所有模块数据
	 */
	BaseModules<T> getModules();

	/**
	 * 设置所有模块
	 *
	 * @param baseModules 模块
	 */
	void setModules(BaseModules<T> baseModules);

	/**
	 * 获取加载器
	 *
	 * @param consumer  消耗函数
	 * @param reference 引用
	 * @return 加载器
	 */
	Runnable getLoader(Consumer<IPlayer<T>> consumer, AtomicReference<BaseModules<T>> reference);

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