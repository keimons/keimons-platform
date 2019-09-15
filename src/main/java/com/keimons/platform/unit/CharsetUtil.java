package com.keimons.platform.unit;

import java.nio.charset.Charset;

public class CharsetUtil {

	private static Charset UTF8 = Charset.forName("UTF-8");

	public static byte[] getUTF8(String string) {
		return string.getBytes(UTF8);
	}

	public static String getUTF8(byte[] bytes) {
		return new String(bytes, UTF8);
	}
}