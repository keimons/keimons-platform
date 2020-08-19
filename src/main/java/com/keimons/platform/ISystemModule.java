package com.keimons.platform;

import java.util.Map;

/**
 * 系统模块
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface ISystemModule {

	void init(Map<KeimonsOption<?>, ?> options);

	void start();

	void close();
}