package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.basic.IPacketStrategy;
import jdk.internal.vm.annotation.ForceInline;

public class JsonPacketPolicy implements IPacketStrategy<JSONObject, JSONObject> {

	@ForceInline
	@Override
	public int findMsgCode(JSONObject packet) {
		return packet.getIntValue("msgCode");
	}

	@ForceInline
	@Override
	public JSONObject findData(JSONObject packet) {
		return packet.getJSONObject("data");
	}

	/**
	 * 构造一个消息体
	 *
	 * @param msgCode 消息号
	 * @param errCode 错误信息
	 * @param message 消息体
	 * @return 消息体
	 */
	public JSONObject createPacket(int msgCode, Object errCode, Object message) {
		JSONObject packet = new JSONObject();
		packet.put("msgCode", msgCode);
		if (errCode != null) {
			packet.put("errCode", msgCode);
		}
		if (message != null) {
			packet.put("data", message);
		}
		return packet;
	}
}