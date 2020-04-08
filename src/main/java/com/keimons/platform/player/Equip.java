package com.keimons.platform.player;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-03-17
 * @since 1.8
 **/
public class Equip implements IBanSetterVersion {
	long equipId;

	public long getEquipId() {
		return equipId;
	}

	public void setEquipId(long equipId) {
		this.equipId = equipId;
	}

	@Override
	public int getVersion() {
		change(Equip::setEquipId, 100);
		return 0;
	}
}
