package com.keimons.platform.system.data;

import com.keimons.platform.keimons.SystemDataManager;
import com.keimons.platform.module.IModule;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 系统数据测试
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class SystemDataTest {

	@BeforeClass
	public static void beforeTest() {
		System.out.println("------------------系统数据模块测试------------------");
		for (int i = 0; i < 10; i++) {
			League league = new League();
			league.setDataId("ServerId_" + i);
			league.setLevel(i + 1);
			league.setNickname("League-" + i);
			SystemDataManager.getSystems().add(league);
		}
	}

	@Test
	public void repeatedTest() {
		IModule<League> module = SystemDataManager.getSystems().findModule(League.class);
		assert module != null;
		System.out.println("联盟数量：" + module.size());

		for (League league : module) {
			System.out.println(league.toString());
		}

		SystemDataManager.getSystems().remove(League.class, "ServerId_5");

		module = SystemDataManager.getSystems().findModule(League.class);
		assert module != null;
		System.out.println("联盟数量：" + module.size());

		for (League league : module) {
			System.out.println(league.toString());
		}
	}

	@Test
	public void singularTest() {
		SystemState state = SystemDataManager.getSystems().get(SystemState.class);
		System.out.println("开服时间：" + state.take("OpenTime"));
	}
}