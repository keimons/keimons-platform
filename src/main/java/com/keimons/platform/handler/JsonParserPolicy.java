package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import jdk.internal.vm.annotation.ForceInline;

public class JsonParserPolicy implements IParserStrategy<JSONObject> {

	@ForceInline
	@Override
	public JSONObject parsePacket(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}
}