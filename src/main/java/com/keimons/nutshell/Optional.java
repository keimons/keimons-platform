package com.keimons.nutshell;

import com.keimons.nutshell.basic.ICodecStrategy;
import com.keimons.nutshell.module.IModuleSerializable;
import com.keimons.nutshell.network.NetService;
import com.keimons.nutshell.network.coder.CodecAdapter;
import com.keimons.nutshell.unit.BaseEnum;

/**
 * 选项
 *
 * @param <T> 选线类型
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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