package com.keimons.platform.module;

import java.util.Map;

/**
 * 数据持久化标识
 * <p>
 * 数据序列化为二进制后的数据，如果要对某一个模块数据进行存储
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IPersistence {

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
	 * @param map 要加载的数据
	 */
	void load(Map<byte[], byte[]> map);
}