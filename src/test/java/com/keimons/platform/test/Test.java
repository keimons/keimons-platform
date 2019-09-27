package com.keimons.platform.test;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.iface.IModule;
import com.keimons.platform.player.AbsPlayer;
import com.keimons.platform.player.BasePlayerData;

/**
 * 系统测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-20
 * @since 1.0
 **/
public class Test extends BasePlayerData {

	public static void main(String[] args) {
		KeimonsServer.start();
	}

	@Override
	public void init(AbsPlayer player) {

	}

	@Override
	public Enum<? extends IModule> getModuleName() {
		return null;
	}
}