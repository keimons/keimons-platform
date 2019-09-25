package com.keimons.platform.player;

import com.keimons.platform.iface.IPlayerData;

/**
 * 玩家数据
 */
public abstract class BasePlayerData implements IPlayerData {
	/**
	 * 计算上次的MD5
	 * <p>
	 * 比较两次的MD5值是否相等，如果相等，则不对这个数据进行存数，否则才存储
	 */
	private String lastMd5;

	public String getLastMd5() {
		return lastMd5;
	}

	public void setLastMd5(String lastMd5) {
		this.lastMd5 = lastMd5;
	}

	@Override
	public void loaded(AbsPlayer player) {
	}
}