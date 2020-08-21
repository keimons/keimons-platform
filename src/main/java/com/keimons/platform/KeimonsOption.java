package com.keimons.platform;

import com.keimons.platform.event.EventService;
import com.keimons.platform.module.IModuleSerializable;
import com.keimons.platform.network.NetService;
import com.keimons.platform.network.coder.CodecAdapter;

/**
 * 选项
 *
 * @param <T> 选线类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsOption<T> {

	/**
	 * TCP连接选项
	 */
	public static final KeimonsOption<NetService> NET = new KeimonsOption<>(true);

	/**
	 * 消息适配器 功能层消息于网络层包体转化器
	 */
	public static final KeimonsOption<CodecAdapter<?>> ADAPTER = new KeimonsOption<>(true);

	/**
	 * 事件系统
	 */
	public static final KeimonsOption<EventService> EVENT = new KeimonsOption<>(false);

	/**
	 * 序列化方案
	 */
	public static final KeimonsOption<IModuleSerializable<?>> SERIALIZE = new KeimonsOption<>(true);

	/**
	 * 是否必要的
	 */
	private final boolean necessary;

	public KeimonsOption(boolean necessary) {
		this.necessary = necessary;
	}

	public boolean isNecessary() {
		return necessary;
	}
}