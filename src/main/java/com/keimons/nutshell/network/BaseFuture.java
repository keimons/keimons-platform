package com.keimons.nutshell.network;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听器映射，建立一个系统框架的监听器和通讯框架监听器的映射。
 *
 * @param <ListenerT> 监听器类型
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class BaseFuture<ListenerT> implements IFuture {

	/**
	 * 监听器映射
	 */
	Map<IFutureListener<? extends IFuture>, ListenerT> listeners;

	/**
	 * 增加系统监听器
	 *
	 * @param listener 系统监听器
	 * @return 网络框架监听器
	 */
	protected ListenerT addListener0(IFutureListener<? extends IFuture> listener) {
		if (listener == null) {
			return null;
		}
		synchronized (this) {
			if (listeners == null) {
				listeners = new HashMap<>();
			}
			return listeners.put(listener, createListener(listener));
		}
	}

	/**
	 * 移除系统监听器
	 *
	 * @param listener 系统监听器
	 * @return 网络框架监听器
	 */
	protected ListenerT removeListener0(IFutureListener<? extends IFuture> listener) {
		if (listener == null) {
			return null;
		}
		synchronized (this) {
			if (listeners != null) {
				return listeners.remove(listener);
			}
		}
		return null;
	}

	/**
	 * 根据系统监听器创建网络监听器
	 *
	 * @param listener 系统监听器
	 * @return 网络监听器
	 */
	@SuppressWarnings("rawtypes")
	protected abstract ListenerT createListener(IFutureListener listener);
}