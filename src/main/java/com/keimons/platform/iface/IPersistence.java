package com.keimons.platform.iface;

/**
 * 数据持久化
 * <p>
 * 数据序列化为二进制后的数据，如果要对某一个模块数据进行存储
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public interface IPersistence {

	/**
	 * 序列化后的数据
	 *
	 * @param notnull 是否强制获取数据
	 * @return 最新数据{@code null}则表示无最新数据
	 */
	byte[] persistence(boolean notnull);
}