package com.keimons.platform.system.data;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.module.ASystemData;
import com.keimons.platform.module.ISingularSystemData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录游戏中的一些状态值
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@ASystemData(moduleName = "state")
public class SystemState implements ISingularSystemData {

	/**
	 * 各种时间值
	 */
	private ConcurrentHashMap<String, Long> times = new ConcurrentHashMap<>();

	public long take(String key) {
		return times.getOrDefault(key, 0L);
	}

	public ConcurrentHashMap<String, Long> getTimes() {
		return times;
	}

	public void setTimes(ConcurrentHashMap<String, Long> times) {
		this.times = times;
	}

	@Override
	public String toString() {
		return "SystemState{" +
				"times=" + JSONObject.toJSONString(times) +
				'}';
	}
}