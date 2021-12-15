package com.keimons.nutshell.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.nutshell.process.AProcessor;
import com.keimons.nutshell.process.IProcessor;
import com.keimons.nutshell.session.Session;

/**
 * 默认实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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