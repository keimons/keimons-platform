package com.keimons.platform.player.data;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.keimons.DefaultPlayer;
import com.keimons.platform.module.ISingularPlayerData;
import com.keimons.platform.module.APlayerData;
import com.keimons.platform.player.IPlayer;

import java.util.HashSet;
import java.util.Set;

/**
 * 任务系统
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@APlayerData(moduleName = "task")
public class Task implements ISingularPlayerData {

	/**
	 * 已经完成的任务
	 */
	private Set<String> finishTasks = new HashSet<>();

	@Override
	public <T extends IPlayer<?>> void init(T player) {
		DefaultPlayer dp = (DefaultPlayer) player;
		System.out.println("玩家ID：" + dp.getIdentifier());
	}

	public Set<String> getFinishTasks() {
		return finishTasks;
	}

	public void setFinishTasks(Set<String> finishTasks) {
		this.finishTasks = finishTasks;
	}

	@Override
	public String toString() {
		return "任务模块，已完成任务：" + JSONObject.toJSONString(finishTasks);
	}
}