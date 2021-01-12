package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
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
}