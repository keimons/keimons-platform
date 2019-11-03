package com.keimons.platform.player;

import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.module.Modules;
import com.keimons.platform.session.Session;

import java.util.Collection;

/**
 * 玩家的接口
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IPlayer {

	/**
	 * 获取用于标识唯一玩家的数据唯一标识符
	 * <p>
	 * 将玩家数据进行持久化时，需要依赖这个这个唯一数据标识符号，这个唯一标识符是不能重复的。
	 * 将数据存储至数据库时，uuid即为标识唯一用户的主键，根据主键对玩家进行索引查询。注意，合
	 * 服后需要依然能够准追找到这个玩家。
	 *
	 * @return 唯一标识符
	 */
	String uuid();

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
	 * 设置所有模块
	 *
	 * @param modules 模块
	 */
	void setModules(Modules modules);

	/**
	 * 设置session
	 *
	 * @param session 客户端-服务器会话
	 */
	void setSession(Session session);

	/**
	 * 获取玩家的一个模块
	 *
	 * @param moduleName 模块名称
	 * @param <T>        模块类型
	 * @return 数据模块
	 */
	<T extends IPlayerData> T getModule(String moduleName);

	/**
	 * 获取玩家所有的模块数据
	 *
	 * @return 玩家所有模块数据
	 */
	Collection<IPlayerData> getModules();
}