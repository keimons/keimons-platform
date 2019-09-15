package com.keimons.platform.unit;

public class StringUtils {

	/**
	 * 判断字符串是否为空
	 *
	 * @param str 字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 判断给定的字符串中是否有空白符
	 *
	 * @param str 字符串
	 * @return 是否有空字符
	 */
	public static boolean hasEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		for (char c : str.toCharArray()) {
			if (Character.isWhitespace(c)) {
				return true;
			}
		}
		return false;
	}
}
