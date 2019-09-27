package com.keimons.platform.player;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IPlayerData;

/**
 * 玩家数据
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
	 * 数据版本是数据迭代的依据，如果数据需要迭代，则先将数据序列化为二进制，再将二级制反序列化为新的版本数据
	 */
	private volatile int version = KeimonsServer.VERSION;

	/**
	 * 获取上次计算的MD5
	 *
	 * @return MD5
	 */
	public String getLastMd5() {
		return lastMd5;
	}

	/**
	 * 设置本次计算的MD5
	 *
	 * @param lastMd5 模块的MD5值
	 */
	public void setLastMd5(String lastMd5) {
		this.lastMd5 = lastMd5;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void loaded(AbsPlayer player) {
	}
}