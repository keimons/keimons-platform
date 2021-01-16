package com.keimons.platform.modular.util;

import com.keimons.platform.handler.BaseHandlerPolicy;
import com.keimons.platform.handler.HandlerManager;
import com.keimons.platform.handler.JsonHandlerPolicy;
import com.keimons.platform.unit.ClassUtil;
import org.junit.Test;

/**
 * Class类工具测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-12-16
 * @since 1.8
 **/
public class ClassUtilTest {

	@Test
	public void test() {
		JsonHandlerPolicy policy = HandlerManager.getHandlerStrategy(0);
		Class<?> type = ClassUtil.findGenericType(policy, BaseHandlerPolicy.class, "SessionT");
		System.out.println(type);
	}
}
