package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import jdk.internal.vm.annotation.ForceInline;

/**
 * JSON包体解析策略
 * <p>
 * 示例程序：使用fastjson将消息解析成{@link com.alibaba.fastjson.JSONObject}对象。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class JsonParserPolicy implements IParserStrategy<JSONObject> {

	@ForceInline
	@Override
	public JSONObject parsePacket(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}
}