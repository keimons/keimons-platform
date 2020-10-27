package com.keimons.platform.modular.handler;

import com.alibaba.fastjson.JSONObject;
import com.keimons.platform.Keimons;
import com.keimons.platform.Optional;
import com.keimons.platform.executor.CodeExecutorPolicy;
import com.keimons.platform.executor.ExecutorManager;
import com.keimons.platform.executor.PoolExecutorPolicy;
import com.keimons.platform.handler.JsonHandlerManager;
import com.keimons.platform.handler.JsonPacketPolicy;
import com.keimons.platform.session.ISession;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.RandomUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Scanner;
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

	@BeforeClass
	public static void beforeTest() throws NoSuchMethodException, IllegalAccessException {
		Keimons.set(Optional.MESSAGE_PARSE, new JsonPacketPolicy());
		ExecutorManager.registerExecutorStrategy(
				POOL_EXECUTOR_STRATEGY, new PoolExecutorPolicy("Pool", 4)
		);
		ExecutorManager.registerExecutorStrategy(
				CODE_EXECUTOR_STRATEGY, new CodeExecutorPolicy("Code", 4)
		);
		JsonHandlerManager.getInstance().addHandler("com.keimons.platform.modular");

		MethodHandle handler = MethodHandles.publicLookup().findVirtual(JsonHandlerManager.class,
				"handler", MethodType.methodType(void.class, ISession.class, byte[].class));
		HandlerTest.handler = MethodHandles.insertArguments(handler, 0, new Session(null));
	}

	@Test
	public void test1() throws Throwable {
		for (int i = 0; i < 10; i++) {
			JSONObject packet = new JSONObject();
			packet.put("account", "test");

			JSONObject request = new JSONObject();
			request.put("msgCode", 1001);
			request.put("data", packet);

			handler.invokeExact(request.toJSONString().getBytes());
		}
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
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

			handler.invokeExact(request.toJSONString().getBytes());
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}