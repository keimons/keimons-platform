package com.keimons.nutshell.proxy;

/**
 * 代理
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IProxy {

	Object getInstance();

	void setResult(Object result);
}