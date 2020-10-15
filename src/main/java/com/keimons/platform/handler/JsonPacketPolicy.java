package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.process.IPacketParseStrategy;

/**
 * Json包体解析策略
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class JsonPacketPolicy implements IPacketParseStrategy<JSONObject, JSONObject> {

	@Override
	public JSONObject parsePacket(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}

	@Override
	public int findMsgCode(JSONObject packet) {
		return packet.getIntValue("msgCode");
	}

	@Override
	public JSONObject findData(JSONObject packet) {
		return packet.getJSONObject("data");
	}
}