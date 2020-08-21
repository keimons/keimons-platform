package com.keimons.platform.player;

/**
 * 数据持久化方案
 * <p>
 * 数据序列化为二进制后的数据，如果要对某一个模块数据进行存储，则需要实现这个接口，并重新这两个方法。
 * 该接口定义了如何存储和加载数据。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IPersistence<T> {

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
	 * 存储
	 * <p>
	 * 对整个数据模块进行存储，将玩家序列化为指定的数据格式，并存储到数据库
	 *
	 * @param coercive 是否强制存储
	 */
	void save(boolean coercive);

	/**
	 * 加载
	 *
	 * @param classes 要加载的数据模块
	 */
	@SuppressWarnings("unchecked")
	void load(Class<? extends IPlayerData>... classes);

	/**
	 * 删除这些模块，模块一旦被删除，将会删除这个模块以及数据库中的数据。
	 *
	 * @param classes 模块
	 */
	@SuppressWarnings("unchecked")
	void remove(Class<? extends IPlayerData>... classes);
}