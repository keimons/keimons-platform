package com.keimons.platform.network;

import com.alibaba.fastjson.JSONObject;
import com.keimons.basic.ICodecStrategy;
import jdk.internal.vm.annotation.ForceInline;

/**
 * JSON包体解析策略
 * <p>
 * 示例程序：使用fastjson将消息解析成{@link JSONObject}对象。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class JsonCodecPolicy implements ICodecStrategy<byte[], JSONObject> {

	@ForceInline
	public JSONObject decode(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}

	@Override
	public byte[] encode(JSONObject packet) throws Exception {
		return packet.toJSONString().getBytes();
	}
}