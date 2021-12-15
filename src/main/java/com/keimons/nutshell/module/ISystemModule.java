package com.keimons.nutshell.module;

import com.keimons.nutshell.Optional;

import java.util.Map;

/**
 * 系统模块
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface ISystemModule {

	void init(Map<Optional<?>, ?> options);

	void start();

	void close();
}