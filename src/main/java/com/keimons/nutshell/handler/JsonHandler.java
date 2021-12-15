package com.keimons.nutshell.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.nutshell.process.IProcessor;
import com.keimons.nutshell.session.Session;

/**
 * Json消息处理器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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