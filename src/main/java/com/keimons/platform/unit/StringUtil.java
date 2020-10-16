package com.keimons.platform.unit;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;

/**
 * 字符串工具类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class StringUtil {

	/**
	 * 标准字符串占位符
	 */
	public static final String PLACEHOLDER = "\u0001";

	/**
	 * 使用{@code \u0001}作为占位符的字符串填充
	 *
	 * @param format 字符串占位符
	 * @param params 填充参数
	 * @return 填充后的字符串
	 */
	public static String format(String format, Object... params) {
		try {
			ConstantCallSite site = (ConstantCallSite) StringConcatFactory.makeConcatWithConstants(MethodHandles.lookup(),
					"makeConcatWithConstants",
					MethodType.genericMethodType(params.length),
					format);
			return (String) site.dynamicInvoker().invokeWithArguments(params);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		String format = "这是一段测试代码，他是为了测试：" +
				"byte(\u0001),boolean(\u0001)" +
				"short(\u0001),int(\u0001)," +
				"float(\u0001),lang(\u0001)," +
				"double(\u0001),String(\u0001)," +
				"Object(\u0001),Class(\u0001)";
		System.out.println(format(format,
				(byte) 1, true,
				(short) -5, 1000,
				-2.5f, 10000L,
				13245.000D, "string",
				new StringUtil(), StringUtil.class
		));
	}
}