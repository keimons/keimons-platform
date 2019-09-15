package com.keimons.platform.unit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RestartTaskThreadPoolExecutor extends ThreadPoolExecutor {

	public RestartTaskThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		//若线程执行某任务失败了，重新提交该任务
		if (t != null) {
			System.out.println("restart task...");
			execute(r);
		}
	}
}