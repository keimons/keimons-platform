package com.keimons.platform.player.data;

import com.keimons.platform.keimons.DefaultPlayer;
import com.keimons.platform.keimons.DefaultSingularModule;
import com.keimons.platform.module.IModule;
import com.keimons.platform.player.BasePlayer;
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

	private static BasePlayer<String> player;

	@BeforeClass
	public static void beforeTest() {
		System.out.println("------------------玩家数据模块测试------------------");
		player = new DefaultPlayer("Test#10001");
		for (int i = 0; i < 10; i++) {
			Equip equip = new Equip();
			equip.setDataId(i);
			equip.setItemId("ItemId." + i);
			equip.setLevel(i + 1);
			equip.setStar(3);
			player.add(equip);
		}
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
		task.getFinishTasks().add("TaskId.1");
		task.getFinishTasks().add("TaskId.2");
		task.getFinishTasks().add("TaskId.3");
		DefaultSingularModule<Task> module = player.findModule(Task.class);
		assert module != null;
		for (Task item : module) {
			System.out.println(item.toString());
		}

		try {
			ThrowableTask throwableTask = player.get(ThrowableTask.class);
			throwableTask.getFinishTasks().add("111");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}