package com.keimons.platform.process;

import com.keimons.platform.session.ISession;
import com.keimons.platform.executor.IExecutorEnum;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息处理器
 * <p>
 * 在这个接口中，仅仅设计了核心的接口，并设计例如getMsgCode() getInterval() getDesc()等接口
 * 考虑到整个项目都会使用{注解-安装}的模式，所以，将协议的描述信息存放在了
 * {@link AProcessor}而不
 * 是在这个接口中实现
 * <p>
 * 在设计模式中，这个接口的作用，也仅仅是用来处理业务逻辑的，不另做其他用途
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public abstract class BaseProcessor<T extends ISession, O> implements IProcessor<T, O> {

	/**
	 * 消息号
	 */
	protected final int msgCode;

	/**
	 * 线程池类型
	 */
	protected final Enum<? extends IExecutorEnum> executor;

	/**
	 * 请求间隔
	 */
	protected final int interval;

	/**
	 * 消息号描述
	 */
	protected final String desc;

	/**
	 * 采样频率
	 */
	protected final int sampling;

	/**
	 * 总的执行时间
	 */
	private int executorTime;

	/**
	 * 采样频率
	 */
	private int[] executorTimes;

	/**
	 * 写入位置
	 */
	private AtomicInteger index = new AtomicInteger(0);

	/**
	 * 与数字，通过计算 index & AND 计算出来下标位置
	 */
	private int AND;

	protected BaseProcessor() {
		AProcessor annotation = this.getClass().getAnnotation(AProcessor.class);
		this.msgCode = annotation.MsgCode();
		this.interval = annotation.Interval();
		this.desc = annotation.Desc();
		this.sampling = annotation.Sampling();
		this.executor = annotation.Executor();

		this.executorTimes = new int[this.sampling];
		this.AND = this.sampling - 1;
	}

	public Enum<? extends IExecutorEnum> getExecutor() {
		return executor;
	}

	/**
	 * 选择线程等级
	 * <p>
	 * 如果线程是自适应等级，则根据历史本消息执行时长，计算出来它应该使用的线程。
	 */
	@Override
	public void processor(T session, O packet) {

	}

	/**
	 * 更新消息的执行时长
	 *
	 * @param executeTime 消息执行时长
	 */
	public void updateExecuteTime(int executeTime) {
		int index = this.index.getAndIncrement() & AND;
		executorTime -= this.executorTimes[index];
		this.executorTimes[index] = executeTime;
		executorTime += executeTime;
	}
}