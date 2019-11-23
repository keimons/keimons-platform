package com.keimons.platform.network;

import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.process.IProcessor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * 消息转化器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class MessageConverter<I> {

	/**
	 * 底层流动消息类型
	 */
	private final Class<I> messageType;

	@SuppressWarnings("unchecked")
	public MessageConverter() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType superclass = (ParameterizedType) type;
			if (superclass.getActualTypeArguments().length > 0) {
				messageType = (Class<I>) superclass.getActualTypeArguments()[0];
			} else {
				throw new ModuleException("未设置系统底层数据传输载体");
			}
		} else {
			throw new ModuleException("未设置系统底层数据传输载体");
		}
	}

	/**
	 * 获取数据由二进制数据转化为程序中数据底层传输载体的数据传输类型
	 * <p>
	 * {@link IProcessor} 将二进制的数据转化为
	 * 程序可识别的载体，并交由消息处理器进行处理。
	 *
	 * @return 传输类型
	 */
	public final Class<I> getMessageType() {
		return messageType;
	}

	/**
	 * 入栈数据转化
	 *
	 * @return 入栈数据格式转化器
	 */
	public abstract IMessageConverter<byte[], I> getInboundConverter();

	/**
	 * 出栈数据转化
	 *
	 * @return 出栈数据格式转化器
	 */
	public abstract IMessageConverter<I, byte[]> getOutboundConverter();

	/**
	 * 获取消息号
	 * <p>
	 * 系统底层的数据传输载体是多样化的，所以我们允许开发人员自定义数据载体格式，
	 * 通过此方法计算出来载体对应的消息号，消息号需要是一个整数。
	 *
	 * @return 消息号转化器
	 */
	public abstract Function<I, Integer> getMsg2CodeConverter();
}