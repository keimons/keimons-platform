package com.keimons.platform.player.data;

import com.keimons.platform.module.IRepeatedPlayerData;
import com.keimons.platform.module.APlayerData;

/**
 * 测试装备
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@APlayerData(moduleName = "equip")
public class Equip implements IRepeatedPlayerData<Integer> {

	/**
	 * 装备唯一ID
	 */
	private int dataId;

	/**
	 * 装备表ID
	 */
	private String itemId;

	/**
	 * 装备等级
	 */
	private int level;

	/**
	 * 装备星级
	 */
	private int star;

	@Override
	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	@Override
	public String toString() {
		return "Equip{" +
				"dataId=" + dataId +
				", itemId='" + itemId + '\'' +
				", level=" + level +
				", star=" + star +
				'}';
	}
}