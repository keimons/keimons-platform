package com.keimons.platform.module;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.unit.CodeUtil;
import com.keimons.platform.unit.MD5Util;

/**
 * 玩家数据
 * <p>
 * 玩家数据的一个抽象实现，实现了数据比对，数据版本记录
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public abstract class BasePlayerData implements IPlayerData {

	/**
	 * 计算上次的MD5
	 * <p>
	 * 比较两次的MD5值是否相等，如果相等，则不对这个数据进行存数，不相等则存储该模块
	 */
	private String lastMd5;

	/**
	 * 当前数据版本
	 * <p>
	 * 数据版本是数据迭代的依据，如果数据需要迭代，则先将数据序列化为二进制，再将二级
	 * 制反序列化为新的版本数据。
	 */
	private volatile int version = KeimonsServer.VERSION;

	@Override
	public byte[] persistence(boolean notnull) {
		byte[] bytes = CodeUtil.encode(this);
		String thisMd5 = MD5Util.md5(bytes);
		if (!notnull && lastMd5 != null && lastMd5.equals(thisMd5)) {
			return null;
		}
		// 可能lastMd5所指向的对象已经进入老年代，所以仅在需要更新对象时才进行更新
		lastMd5 = thisMd5;
		return bytes;
	}

	@Override
	public int getVersion() {
		return version;
	}
}