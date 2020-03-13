package com.keimons.platform.keimons;

import com.keimons.platform.process.SelectionThreadFunction;
import com.keimons.platform.process.IHandler;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * Java的消息号实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultJavaHandler implements IHandler<Session, Object> {

	/**
	 * 消息号
	 */
	private int msgCode;

	/**
	 * 消息处理器
	 */
	private IProcessor<Session, Object> processor;

	/**
	 * 描述信息
	 */
	private String desc;

	/**
	 * 协议请求间隔
	 */
	protected int interval;

	/**
	 * 执行类型
	 */
	protected DefaultExecutorEnum executorType;

	@Override
	public Runnable createTask(Session session, Object message) {
		return () -> {
			try {
				processor.processor(session, message);
			} finally {
				session.finish();
			}
		};
	}

	@Override
	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

	public IProcessor<Session, Object> getProcessor() {
		return processor;
	}

	public void setProcessor(IProcessor<Session, Object> processor) {
		this.processor = processor;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public SelectionThreadFunction<Session, Object> getRule() {
		return null;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public DefaultExecutorEnum getExecutorType() {
		return executorType;
	}

	public void setExecutorType(DefaultExecutorEnum executorType) {
		this.executorType = executorType;
	}
}