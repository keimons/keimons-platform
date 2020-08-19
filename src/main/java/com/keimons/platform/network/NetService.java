package com.keimons.platform.network;

import org.jetbrains.annotations.Range;

/**
 * 网络服务
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface NetService {

	/**
	 * 启动服务
	 *
	 * @param port 监听端口号
	 */
	void start(@Range(from = 1, to = 65535) int port);
}