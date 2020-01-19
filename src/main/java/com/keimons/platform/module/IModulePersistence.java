package com.keimons.platform.module;

import com.keimons.platform.iface.IData;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.player.IModule;

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
public interface IModulePersistence<T> extends IData {

	/**
	 * 增加一个模块
	 *
	 * @param module   模块
	 * @param coercive 是否强制存储
	 * @throws IOException 序列化异常
	 */
	void setModule(IModule<? extends IPlayerData> module, boolean coercive) throws IOException;

	/**
	 * 是否压缩
	 *
	 * @return 是否压缩
	 */
	boolean isCompress();

	/**
	 * 获取存库数据
	 * <p>
	 * 获取合并后的数据，这是存入数据库的最终数据。
	 *
	 * @return 存库数据
	 * @throws IOException 序列化错误
	 */
	T serialize() throws IOException;
}