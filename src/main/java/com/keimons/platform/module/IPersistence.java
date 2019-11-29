package com.keimons.platform.module;

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

	void save(String identifier, boolean coercive);
}