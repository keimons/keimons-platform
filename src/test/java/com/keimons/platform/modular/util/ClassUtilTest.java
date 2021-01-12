package com.keimons.platform.modular.util;

import com.keimons.platform.unit.ClassUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-12-16
 * @since 1.8
 **/
public class ClassUtilTest {

	@Test
	public void test() {
		Map<Integer, Integer> map = new HashMap<>();
		Class<?> type = ClassUtil.findGenericType(map, HashMap.class, "K");
		System.out.println(type);
	}
}