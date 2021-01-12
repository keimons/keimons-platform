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
public class JsonHandlerPolicy extends BaseHandlerPolicy<Session, JSONObject, JSONObject> {

	public JsonHandlerPolicy(int strategyIndex) {
		super(strategyIndex);
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
	public void exceptionCaught(Session session, JSONObject packet, Throwable cause) {
		cause.printStackTrace();
	}
}