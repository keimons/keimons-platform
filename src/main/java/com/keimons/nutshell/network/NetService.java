package com.keimons.nutshell.network;

import org.jetbrains.annotations.Range;

/**
 * 网络服务
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface NetService {

	/**
	 * 启动服务
	 *
	 * @param port 监听端口号
	 */
	void start(@Range(from = 1, to = 65535) int port);
}