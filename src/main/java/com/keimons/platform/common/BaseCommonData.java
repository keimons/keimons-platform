package com.keimons.platform.common;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IData;
import com.keimons.platform.iface.IDataVersion;
import com.keimons.platform.iface.ILoaded;
import com.keimons.platform.player.AbsPlayer;

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
public abstract class BaseCommonData implements IData, ILoaded, IDataVersion {

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

	@Override
	public void loaded(AbsPlayer player) {
	}
}