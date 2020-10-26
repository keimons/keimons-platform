package com.keimons.platform.system.data;

import com.keimons.platform.module.ASystemData;
import com.keimons.platform.module.IRepeatedSystemData;

/**
 * 联盟
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@ASystemData(moduleName = "league")
public class League implements IRepeatedSystemData<String> {

	private String dataId;

	private String nickname;

	private int level;

	@Override
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "League{" +
				"dataId='" + dataId + '\'' +
				", nickname='" + nickname + '\'' +
				", level=" + level +
				'}';
	}
}