package com.keimons.platform.modular.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.executor.CommitterManager;
import com.keimons.platform.process.AProcessor;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * 登录请求测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-10-26
 * @since 1.8
 **/
@AProcessor(
		MsgCode = 1001,
		CommitterStrategy = CommitterManager.LOCATE_TASK_COMMITTER_POLICY,
		ExecutorStrategy = HandlerTest.CODE_EXECUTOR_STRATEGY
)
public class LoginProcessor1001 implements IProcessor<Session, JSONObject> {

	@Override
	public void processor(Session session, JSONObject params) {
		System.out.println("执行线程：" + Thread.currentThread().getName());
	}

	@Override
	public int threadCode(Session session, JSONObject params) {
		return 1009;
	}
}