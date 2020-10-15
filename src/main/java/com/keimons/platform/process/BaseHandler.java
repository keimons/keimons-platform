package com.keimons.platform.process;

import com.keimons.platform.session.ISession;
import com.keimons.platform.unit.ClassUtil;

/**
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BaseHandler<SessionT extends ISession, DataT, MessageT>
		implements IHandler<SessionT, DataT, MessageT> {

	/**
	 * 消息号
	 */
	protected final int msgCode;

	/**
	 * 消息描述
	 */
	protected final String desc;

	/**
	 * 协议请求间隔
	 */
	protected int interval;

	/**
	 * 包体类型
	 */
	protected final Class<DataT> dataType;

	/**
	 * 消息体类型
	 */
	protected final Class<MessageT> messageType;

	/**
	 * 消息处理器
	 */
	protected final IProcessor<SessionT, MessageT> processor;

	public BaseHandler(int msgCode, int interval, String desc,
					   IProcessor<SessionT, MessageT> processor) {
		this.msgCode = msgCode;
		this.interval = interval;
		this.desc = desc;
		this.processor = processor;

		this.dataType = ClassUtil.findGenericType(
				this, IHandler.class, "DataT"
		);
		this.messageType = ClassUtil.findGenericType(
				processor, IProcessor.class, "MessageT"
		);
	}

	@Override
	public int getMsgCode() {
		return msgCode;
	}

	@Override
	public IProcessor<SessionT, MessageT> getProcessor() {
		return processor;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public Class<DataT> getDataType() {
		return dataType;
	}

	@Override
	public Class<MessageT> getMessageType() {
		return messageType;
	}
}