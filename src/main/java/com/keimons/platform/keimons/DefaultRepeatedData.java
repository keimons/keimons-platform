package com.keimons.platform.keimons;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IRepeatedData;
import com.keimons.platform.module.IBytesPlayerDataSerializable;
import com.keimons.platform.unit.CodeUtil;

import java.io.IOException;

/**
 * 玩家数据
 * <p>
 * 玩家数据的一个抽象实现，实现了数据比对，数据版本记录
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public abstract class DefaultRepeatedData<T> implements IRepeatedData<T>, IBytesPlayerDataSerializable {

	/**
	 * 当前数据版本
	 * <p>
	 * 数据版本是数据迭代的依据，如果数据需要迭代，则先将数据序列化为二进制，再将二级
	 * 制反序列化为新的版本数据。
	 */
	private volatile transient int version = KeimonsServer.VERSION;

	@Override
	public byte[] serialize(boolean notnull) throws IOException {
		return CodeUtil.encode(this);
	}

	@Override
	public int getVersion() {
		return version;
	}
}