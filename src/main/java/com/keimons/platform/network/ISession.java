package com.keimons.platform.network;

/**
 * 连接
 *
 * @author monkey1993
 * @version 1.0
 * @date 2021-01-29
 * @since 1.8
 **/
public interface ISession<ConnectT> {

	IFuture write(Object object);

	IFuture write(Object object, Object object2);
}