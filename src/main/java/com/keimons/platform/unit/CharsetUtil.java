package com.keimons.platform.unit;

import java.nio.charset.Charset;

/**
 * 字符串编码工具类
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-25
 * @since 1.8
 */
public class CharsetUtil {

	/**
	 * UTF8编码
	 */
	private static Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * 根据字符串获得UTF8编码的字节数组
	 *
	 * @param string 字符串
	 * @return 字节数组
	 */
	public static byte[] getUTF8(String string) {
		return string.getBytes(UTF8);
	}

	/**
	 * 根据字节数组获取UTF8编码的字符串
	 *
	 * @param bytes
	 * @return
	 */
	public static String getUTF8(byte[] bytes) {
		return new String(bytes, UTF8);
	}
}