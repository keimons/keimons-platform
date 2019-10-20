package com.keimons.platform.network;

public interface ICoder<T, R> {

	R coder(T data) throws Exception;
}