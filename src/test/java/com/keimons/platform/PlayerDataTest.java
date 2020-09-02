package com.keimons.platform;

import com.keimons.platform.keimons.DefaultPlayer;
import com.keimons.platform.module.IModule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 玩家数据测试
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class PlayerDataTest {

	private static DefaultPlayer player;

	@BeforeClass
	public static void beforeTest() {
		System.out.println("------------------开始玩家数据模块测试------------------");
		player = new DefaultPlayer("Test#10001");
		for (int i = 0; i < 10; i++) {
			Equip equip = new Equip();
			equip.setDataId(i);
			equip.setItemId("ItemId." + i);
			equip.setLevel(i + 1);
			equip.setStar(3);
			player.addRepeatedData(equip);
		}
		player.add(new Task());
	}

	@Test
	public void repeatedTest() {

		Equip equip = player.get(Equip.class, 1);
		assert equip != null;
		System.out.println(equip.getItemId());

		IModule<Equip> module = player.findModule(Equip.class);
		assert module != null;
		System.out.println("装备数量：" + module.size());

		for (Equip item : module) {
			System.out.println(item.toString());
		}

		player.remove(Equip.class, 1);
		System.out.println("装备是否移除：" + (player.get(Equip.class, 1) == null));

		module = player.findModule(Equip.class);
		assert module != null;
		System.out.println("装备数量：" + module.size());

		for (Equip item : module) {
			System.out.println(item.toString());
		}
	}

	@Test
	public void singularTest() {
		Task task = player.get(Task.class);
		assert task != null;
		task.getFinishTasks().add("TaskId.1");
		task.getFinishTasks().add("TaskId.2");
		task.getFinishTasks().add("TaskId.3");
		IModule<Task> module = player.findModule(Task.class);
		assert module != null;
		for (Task item : module) {
			System.out.println(item.toString());
		}
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("------------------完成玩家数据模块测试------------------");
	}
}