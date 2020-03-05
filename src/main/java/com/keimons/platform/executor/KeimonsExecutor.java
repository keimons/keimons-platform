package com.keimons.platform.executor;

import com.keimons.platform.log.LogService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 业务处理线程模型
 * <p>
 * 线程安全的
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class KeimonsExecutor {

	private final Map<? extends Enum<? extends IExecutorEnum>, Object> executors;

	@SuppressWarnings("unchecked")
	public <T extends Enum<T>> KeimonsExecutor(Class<? extends IExecutorEnum> clazz) {
		Map<T, Object> executors = new HashMap<>();
		try {
			Method values = clazz.getMethod("values");
			T[] executorsInfo = (T[]) values.invoke(null);
			for (T executorInfo : executorsInfo) {
				IExecutorEnum info = (IExecutorEnum) executorInfo;
				if (info.isRoute()) {
					ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(info.getThreadNumb());
					Executor[] routeExecutors = new Executor[info.getThreadNumb()];
					for (int i = 0; i < info.getThreadNumb(); i++) {
						routeExecutors[i] = new Executor();
						service.execute(routeExecutors[i]);
					}
					executors.put(executorInfo, routeExecutors);
				} else {
					if (info.getThreadNumb() == 1) {
						ExecutorService service = Executors.newSingleThreadExecutor();
						executors.put(executorInfo, service);
					} else {
						ExecutorService service = Executors.newFixedThreadPool(info.getThreadNumb());
						executors.put(executorInfo, service);
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		this.executors = (Map<? extends Enum<? extends IExecutorEnum>, Object>) new EnumMap<>(executors);
	}

	/**
	 * 执行消息
	 *
	 * @param type     线程池类型
	 * @param runnable 线程池昵称
	 */
	public void execute(Enum<? extends IExecutorEnum> type, Runnable runnable) {
		ExecutorService service = (ExecutorService) this.executors.get(type);
		service.execute(runnable);
	}

	public void execute(Enum<? extends IExecutorEnum> type, int route, Runnable runnable) {
		Executor[] executor = (Executor[]) this.executors.get(type);
		executor[route].add(runnable);
	}

	/**
	 * 选择执行器并执行消息体
	 *
	 * @param type     线程类型
	 * @param callable 执行内容
	 * @param <R>      返回值类型
	 * @return 执行结果
	 */
	public <R> R execute(Enum<? extends IExecutorEnum> type, Callable<R> callable) throws ExecutionException, InterruptedException {
		if (((IExecutorEnum) type).isRoute()) {
			ExecutorService service = (ExecutorService) this.executors.get(type);
			return service.submit(callable).get();
		} else {
			return null;
		}
	}

	/**
	 * 消息执行队列
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 **/
	static class Executor implements Runnable {

		/**
		 * 线程安全的阻塞队列
		 */
		private final BlockingDeque<Runnable> queue;

		/**
		 * 执行器
		 * <p>
		 * 消息的真正执行者
		 */
		public Executor() {
			queue = new LinkedBlockingDeque<>();
		}

		/**
		 * 是否执行中
		 */
		private boolean run = true;

		@Override
		public void run() {
			while (run) {
				try {
					Runnable runnable = queue.take();
					runnable.run();
				} catch (Throwable e) {
					LogService.error(e);
				}
			}
		}

		/**
		 * 增加一个任务
		 *
		 * @param runnable 队尾
		 */
		public void add(Runnable runnable) {
			queue.add(runnable);
		}

		/**
		 * 增加一个任务
		 *
		 * @param callable 队尾
		 * @return 是否成功
		 */
		public <T> T offer(Callable<T> callable) throws ExecutionException, InterruptedException {
			FutureTask<T> task = new FutureTask<>(callable);
			queue.offer(task);
			return task.get();
		}
	}
}