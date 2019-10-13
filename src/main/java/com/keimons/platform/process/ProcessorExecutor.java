package com.keimons.platform.process;

import com.keimons.platform.log.LogService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息执行
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProcessorExecutor implements Runnable {

	private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private boolean run = true;

	@Override
	public void run() {
		while (run) {
			try {
				Runnable reqs = queue.take();
				reqs.run();
			} catch (Throwable e) {
				LogService.error(e);
			}
		}
	}

	public void add(Runnable run) {
		queue.add(run);
	}
}