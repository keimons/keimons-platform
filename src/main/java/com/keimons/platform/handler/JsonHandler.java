package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * Json消息处理器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class JsonHandler extends BaseHandler<Session, JSONObject, JSONObject> {

	public JsonHandler(IProcessor<Session, JSONObject> processor,
					   int msgCode, int taskStrategy, int executorStrategy, int interval, String desc) {
		super(processor, msgCode, taskStrategy, executorStrategy, interval, desc);
	}

	@Override
	public JSONObject parseMessage(JSONObject packet) throws Exception {
		return packet;
	}
}