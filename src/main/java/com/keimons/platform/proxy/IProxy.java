package com.keimons.platform.proxy;

/**
 * 代理
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IProxy {

	Object getInstance();

	void setResult(Object result);
}