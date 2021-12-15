package com.keimons.nutshell.unit;

import java.net.URL;

/**
 * 路径工具包
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class URLUtil {

	public static URL packageToURL(String pkg) {
		String fileName = "/" + pkg.replaceAll("\\.", "/");
		return URLUtil.class.getResource(fileName);
	}
}