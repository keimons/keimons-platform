package com.keimons.platform.log;

/**
 * 线程本地日志系统
 * <p>
 * 用于任务执行中的一些数据，例如，某些信息只希望在某些情况下才被打印。
 * 线程日志不会创建额外的对象，但是，如果一个对象被存入线程本地日志系统中，那么它将不会被释放，只会
 * 等待数据被新的数据覆盖才会被回收。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class ThreadLocalErrorLogger {

	private static final ThreadLocal<RingReference> info = new ThreadLocal<>();

	public void start() {

	}

	public void close() {

	}

	/**
	 * 记录一条线程本地日志
	 *
	 * @param param 日志参数
	 */
	public void logger(Object param) {
		RingReference ring = ThreadLocalErrorLogger.info.get();
		if (ring == null) {
			ring = new RingReference(32);
			ThreadLocalErrorLogger.info.set(ring);
		}
		ring.offer(param);
	}

	public void getLog() {
	}

	/**
	 * 环形引用 这个引用不会立即销毁对象
	 *
	 * @author monkey1993
	 * @version 1.0
	 * @since 1.8
	 */
	private static class RingReference {

		final int size;

		final Object[] object;

		final int mask;

		int writerIndex;

		int readerIndex;

		public RingReference(int size) {
			this.size = size;
			this.object = new Object[size];
			this.mask = size - 1;
		}

		/**
		 * 在队尾追加一个引用
		 *
		 * @param item 追加的引用
		 */
		void offer(Object item) {
			if (writerIndex - readerIndex >= size) {
				readerIndex++;
			}
			object[writerIndex++ & mask] = item;
		}

		void take() {

		}
	}
}