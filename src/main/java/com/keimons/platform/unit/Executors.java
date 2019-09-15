package com.keimons.platform.unit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Executors {

	/**
	 * 单线程线程池
	 *
	 * @return 线程池服务
	 */
	public static ExecutorService newSingleThreadExecutor() {
		return new RestartTaskThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>());
	}
}