package com.keimons.platform;

import com.keimons.basic.ICodecStrategy;
import com.keimons.platform.module.IModuleSerializable;
import com.keimons.platform.network.NetService;
import com.keimons.platform.network.coder.CodecAdapter;
import com.keimons.platform.unit.BaseEnum;

/**
 * 选项
 *
 * @param <T> 选线类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Optional<T> extends BaseEnum<Optional<T>> {

	/**
	 * 网络层
	 */
	public static Optional<NetService> NET;

	/**
	 * 消息适配器 功能层消息于网络层包体转化器
	 */
	public static final Optional<CodecAdapter<?>> ADAPTER;

	/**
	 * 序列化方案
	 */
	public static final Optional<IModuleSerializable<?>> SERIALIZE;

	/**
	 * 消息解析策略
	 */
	public static final Optional<ICodecStrategy> MESSAGE_PARSE;

	static {
		NET = new Optional<>("NET", 0);
		ADAPTER = new Optional<>("ADAPTER", 1);
		SERIALIZE = new Optional<>("SERIALIZE", 2);
		MESSAGE_PARSE = new Optional<>("MESSAGE_PARSE", 3);
	}

	private T option;

	private Optional(String name, int ordinal) {
		super(name, ordinal);
	}

	@Override
	public int size() {
		return 13;
	}

	public T getOption() {
		return option;
	}

	public void setOption(T option) {
		this.option = option;
	}
}