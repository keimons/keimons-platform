package com.keimons.nutshell.network;

import com.alibaba.fastjson.JSONObject;
import com.keimons.nutshell.basic.ICodecStrategy;

/**
 * JSON包体解析策略
 * <p>
 * 示例程序：使用fastjson将消息解析成{@link JSONObject}对象。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class JsonCodecPolicy implements ICodecStrategy<byte[], JSONObject> {

	@Override
	public JSONObject decode(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}

	@Override
	public byte[] encode(JSONObject packet) throws Exception {
		return packet.toJSONString().getBytes();
	}
}