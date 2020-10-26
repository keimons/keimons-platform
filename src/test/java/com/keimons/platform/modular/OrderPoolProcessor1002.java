package com.keimons.platform.modular;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.process.AProcessor;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * 顺序请求测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-10-26
 * @since 1.8
 **/
@AProcessor(
		MsgCode = 1002,
		ExecutorStrategy = HandlerTest.POOL_EXECUTOR_STRATEGY
)
public class OrderPoolProcessor1002 implements IProcessor<Session, JSONObject> {

	@Override
	public void processor(Session session, JSONObject params) {
		int number = params.getIntValue("number");
		if (number != HandlerTest.NUMBER.getAndIncrement()) {
			System.err.println("顺序错误！");
			System.exit(0);
		}
		System.out.println("消息号：1002，执行线程：" + Thread.currentThread().getName() + "，顺序：" + number);
	}
}