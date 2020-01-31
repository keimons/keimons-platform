package com.keimons.platform.player;

import java.util.function.Consumer;

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
	 * 存储
	 * <p>
	 * 对整个数据模块进行存储，将玩家序列化为指定的数据格式，并存储到数据库
	 *
	 * @param coercive 是否强制存储
	 */
	void save(boolean coercive);

	/**
	 * 加载器
	 *
	 * @return 加载器
	 */
	Consumer<T> getLoader();
}