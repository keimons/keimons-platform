package com.keimons.platform.modular.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.executor.CodeExecutorPolicy;
import com.keimons.platform.executor.ExecutorManager;
import com.keimons.platform.executor.PoolExecutorPolicy;
import com.keimons.platform.handler.HandlerManager;
import com.keimons.platform.handler.JsonHandlerPolicy;
import com.keimons.platform.session.ISession;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.RandomUtil;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息号测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-10-26
 * @since 1.8
 **/
public class HandlerTest {

	public static final int POOL_EXECUTOR_STRATEGY = 1;

	public static final int CODE_EXECUTOR_STRATEGY = 2;

	public static AtomicInteger NUMBER = new AtomicInteger();

	public static MethodHandle handler;

	static {
		if (ExecutorManager.getExecutorStrategy(POOL_EXECUTOR_STRATEGY) == null) {
			ExecutorManager.registerExecutorStrategy(
					POOL_EXECUTOR_STRATEGY, new PoolExecutorPolicy("Pool", 4)
			);
		}
		if (ExecutorManager.getExecutorStrategy(CODE_EXECUTOR_STRATEGY) == null) {
			ExecutorManager.registerExecutorStrategy(
					CODE_EXECUTOR_STRATEGY, new CodeExecutorPolicy("Code", 4)
			);
		}
		JsonHandlerPolicy policy = HandlerManager.getHandlerStrategy(HandlerManager.JSON_HANDLER);
		policy.addHandler("com.keimons.platform.modular");

		try {
			MethodHandle handler = MethodHandles.publicLookup().findVirtual(JsonHandlerPolicy.class,
					"handler", MethodType.methodType(void.class, ISession.class, Object.class));
			handler = MethodHandles.insertArguments(handler, 0, policy);
			handler = MethodHandles.insertArguments(handler, 0, new Session(null));
			HandlerTest.handler = handler;
		} catch (NoSuchMethodException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test1() throws Throwable {
		for (int i = 0; i < 10; i++) {
			JSONObject packet = new JSONObject();
			packet.put("account", "test");

			JSONObject request = new JSONObject();
			request.put("msgCode", 1001);
			request.put("data", packet);

			handler.invokeWithArguments(request);
		}
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	@Test
	public void test2() throws Throwable {
		for (int i = 0; i < 10000; i++) {
			JSONObject packet = new JSONObject();
			packet.put("number", i);

			JSONObject request = new JSONObject();
			if (RandomUtil.nextInt(100) < 50) {
				request.put("msgCode", 1002);
			} else {
				request.put("msgCode", 1003);
			}
			request.put("data", packet);

			handler.invokeWithArguments(request);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
