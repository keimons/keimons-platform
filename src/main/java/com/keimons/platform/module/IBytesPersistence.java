package com.keimons.platform.module;

import java.io.IOException;

/**
 * 二进制数据持久化
 * <p>
 * 将玩家数据序列化为二进制后的数据。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IBytesPersistence {

	/**
	 * 获取持久化数据
	 *
	 * @param notnull 是否强制获取数据
	 * @return 最新数据{@code null}则表示无最新数据
	 * @throws IOException 序列化错误
	 */
	byte[] persistence(boolean notnull) throws IOException;
}