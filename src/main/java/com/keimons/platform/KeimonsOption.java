package com.keimons.platform;

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
public class KeimonsOption<T> extends BaseEnum<KeimonsOption<T>> {

	/**
	 * 网络层
	 */
	public static KeimonsOption<NetService> NET;

	/**
	 * 消息适配器 功能层消息于网络层包体转化器
	 */
	public static final KeimonsOption<CodecAdapter<?>> ADAPTER;

	/**
	 * 序列化方案
	 */
	public static final KeimonsOption<IModuleSerializable<?>> SERIALIZE;

	static {
		NET = new KeimonsOption<>("NET", 0);
		ADAPTER = new KeimonsOption<>("ADAPTER", 1);
		SERIALIZE = new KeimonsOption<>("SERIALIZE", 2);
	}

	private T option;

	private KeimonsOption(String name, int ordinal) {
		super(name, ordinal);
	}

	public T getOption() {
		return option;
	}

	public void setOption(T option) {
		this.option = option;
	}
}