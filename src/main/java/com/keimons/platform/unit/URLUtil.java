package com.keimons.platform.unit;

import java.net.URL;

/**
 * 路径工具包
 *
 * @version 1.0
 * @since 1.8
 **/
public class URLUtil {

	public static URL packageToURL(String pkg) {
		String fileName = "/" + pkg.replaceAll("\\.", "/");
		return URLUtil.class.getResource(fileName);
	}
}