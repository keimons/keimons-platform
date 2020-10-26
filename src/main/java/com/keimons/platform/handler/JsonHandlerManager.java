package com.keimons.platform.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.process.AProcessor;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * 默认实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class JsonHandlerManager extends BaseHandlerManager<Session, JSONObject, JSONObject> {

	private static final JsonHandlerManager instance = new JsonHandlerManager();

	private JsonHandlerManager() {

	}

	public static JsonHandlerManager getInstance() {
		return instance;
	}

	/**
	 * 加载所有消息号
	 *
	 * @param pkg 包体
	 */
	public void addHandler(String pkg) {
		super.<AProcessor, JSONObject>addHandler(pkg, AProcessor.class, (clazz, annotation) -> {
			try {
				IProcessor<Session, JSONObject> processor = clazz.getDeclaredConstructor().newInstance();
				return new JsonHandler(
						processor,
						annotation.MsgCode(),
						annotation.CommitterStrategy(),
						annotation.ExecutorStrategy(),
						annotation.Interval(),
						annotation.Desc()
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Override
	public void exceptionCaught(Session session, byte[] raw, Throwable cause) {
		cause.printStackTrace();
	}
}