package com.keimons.platform.game;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.iface.IGameData;

/**
 * 公共数据
 * <p>
 * 公共数据的一个抽象实现，实现了数据比对，数据版本记录
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-10-02
 * @since 1.0
 */
@AGameData
public abstract class BaseGameData implements IGameData {

	/**
	 * 当前数据版本
	 * <p>
	 * 数据版本是数据迭代的依据，如果数据需要迭代，则先将数据序列化为二进制，再将二级制反序列化为新的版本数据
	 */
	private volatile int version = KeimonsServer.VERSION;

	@Override
	public int getVersion() {
		return version;
	}
}