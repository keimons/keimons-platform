package com.keimons.platform.process;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.session.Session;
import com.keimons.platform.unit.TimeUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述协议的信息
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProcessorInfo<T> {

	/**
	 * 分界线 1级线程和2级线程的分界线
	 */
	private static int TOP_MID_LIMIT;

	/**
	 * 分界线 2级线程和3级线程的分界线
	 */
	private static int MID_LOW_LIMIT;

	/**
	 * 锁，防止消息出现数据错乱执行线程错误
	 */
	private Lock lock = new ReentrantLock();

	/**
	 * 协议号 {@link AProcessor#MsgCode()}
	 */
	private int msgCode;

	/**
	 * 协议接受的间隔时间 {@link AProcessor#Interval()}
	 */
	private int interval;

	/**
	 * 线程优先级 {@link AProcessor#ThreadLevel()}
	 */
	private ThreadLevel threadLevel;

	/**
	 * 采样频率 {@link AProcessor#Sampling()}
	 */
	private int sampling;

	/**
	 * 消息处理器
	 */
	private BaseProcessor<T> processor;

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
	private int index = 0;

	/**
	 * 与数字，通过计算 index & AND 计算出来下标位置
	 */
	private int AND;

	public ProcessorInfo(AProcessor info, BaseProcessor<T> processor) {
		if (info.ThreadLevel() == ThreadLevel.AUTO &&
				(info.Sampling() <= 0 || (info.Sampling() & (info.Sampling() - 1)) != 0)) {
			throw new NumberFormatException("必须是2的整数次幂");
		}
		this.AND = info.Sampling() - 1;
		this.msgCode = info.MsgCode();
		this.interval = info.Interval();
		this.threadLevel = info.ThreadLevel();
		this.sampling = info.Sampling();
		this.processor = processor;
		executorTimes = new int[info.Sampling()];
		TOP_MID_LIMIT = KeimonsServer.KeimonsConfig.getNetThreadLevel()[0];
		MID_LOW_LIMIT = KeimonsServer.KeimonsConfig.getNetThreadLevel()[1];
	}

	/**
	 * 选择线程等级
	 * <p>
	 * 如果线程是自适应等级，则根据历史本消息执行时长，计算出来它应该使用的线程。
	 *
	 * @return 线程等级
	 */
	public ThreadLevel selectThreadLevel() {
		if (threadLevel == ThreadLevel.AUTO) {
			int executeTime;
			lock.lock();
			executeTime = executorTime / sampling;
			lock.unlock();
			if (executeTime < TOP_MID_LIMIT) {
				return ThreadLevel.H_LEVEL;
			} else if (executeTime < MID_LOW_LIMIT) {
				return ThreadLevel.M_LEVEL;
			} else {
				return ThreadLevel.L_LEVEL;
			}
		}
		return threadLevel;
	}

	/**
	 * 更新消息的执行时长
	 *
	 * @param executeTime 消息执行时长
	 */
	public void updateExecuteTime(int executeTime) {
		if (threadLevel == ThreadLevel.AUTO) {
			lock.lock();
			executorTime -= this.executorTimes[index];
			this.executorTimes[index] = executeTime;
			executorTime += executeTime;
			index++;
			index = index & AND;
			lock.unlock();
		}
	}

	/**
	 * 处理业务逻辑
	 *
	 * @param session 会话
	 * @param packet  消息
	 */
	public void processor(Session session, T packet) {
		try {
			long requestTime = TimeUtil.currentTimeMillis();
			if (session.intervalVerifyAndUpdate(msgCode, requestTime, interval)) {
				// TODO session.send(msgCode, null, "FrequentRequestError");
				return;
			}
			processor.processor(session, packet);
			long end = TimeUtil.currentTimeMillis();
			if (end - requestTime > 100) {
				LogService.info(TimeUtil.getDateTime() + " 超长消息执行：" + msgCode + "，执行时长：" + (end - requestTime));
			}
		} catch (Exception e) {
			LogService.error(e);
		}
	}

	/**
	 * 获取旅游线程
	 *
	 * @param session  会话
	 * @param packet   消息体
	 * @param maxIndex 最大线程ID
	 * @return 线程ID
	 */
	public int getRoute(Session session, T packet, int maxIndex) {
		return processor.route(session, packet, maxIndex);
	}
}