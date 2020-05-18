package com.keimons.platform.executor;

import com.keimons.platform.unit.UnsafeUtil;
import sun.misc.Unsafe;

/**
 * 环形Buffer计数器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
class Sequencer {

	private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();

	/**
	 * 读取的字段
	 */
	private static final long READER_VALUE;

	/**
	 * 写入的字段
	 */
	private static final long WRITER_VALUE;

	static {
		long reader;
		try {
			reader = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("readerIndex"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			reader = 0;
		}
		READER_VALUE = reader;

		long writer;
		try {
			writer = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("writerIndex"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			writer = 0;
		}
		WRITER_VALUE = writer;
	}

	/**
	 * boolean[]中首元素的地址
	 */
	private static final long BASE = UNSAFE.arrayBaseOffset(boolean[].class);

	/**
	 * boolean[]中每个元素占用字节数
	 */
	private static final long SCALE = UNSAFE.arrayIndexScale(boolean[].class);

	/**
	 * 环形Buffer的元素数量
	 */
	private final int bufferSize;

	/**
	 * 最大存放位置
	 */
	private final int mask;

	/**
	 * 写入的位置
	 */
	private volatile int writerIndex;

	/**
	 * 读取的位置
	 */
	private volatile int readerIndex;

	/**
	 * 标记的位置
	 */
	private final boolean[] maskerIndex;

	public Sequencer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.mask = bufferSize - 1;
		this.maskerIndex = new boolean[bufferSize];
	}

	/**
	 * 获取存入的位置
	 *
	 * @return 存入位置
	 */
	public int next() {
		for (; ; ) {
			int newIndex = writerIndex + 1;
			int index = (newIndex - 1) & mask;
			// 使用volatile语义，可以保证获取到的始终是最新值 计算环形Buffer的下一个下标是否越界
			if (UNSAFE.getBooleanVolatile(maskerIndex, (index * SCALE) + BASE)) {
				// TODO 环形Buffer没有可写入的位置 需要补充策略
				return -1;
			} else {
				if (UNSAFE.compareAndSwapInt(this, WRITER_VALUE, writerIndex, newIndex)) {
					return index;
				}
			}
		}
	}

	/**
	 * 记录一个标记
	 *
	 * @param index     标记
	 * @param available 是否活跃的
	 */
	public void mark(int index, int available) {
		UNSAFE.putOrderedInt(maskerIndex, (index * SCALE) + BASE, available);
	}

	/**
	 * 获取取出的位置
	 *
	 * @return 取出位置
	 */
	public int take() {
		for (; ; ) {
			int nextIndex = readerIndex + 1;
			if (nextIndex - writerIndex > 0) {
				// TODO 环形Buffer没有可读取的位置 需要补充策略
				return -1;
			} else {
				if (UNSAFE.compareAndSwapInt(this, READER_VALUE, readerIndex, nextIndex)) {
					return (nextIndex - 1) & mask;
				}
			}
		}
	}

	/**
	 * 获取元素个数
	 *
	 * @return 元素个数
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * 获取当前数组中的元素数量
	 *
	 * @return 元素数量
	 */
	public int size() {
		return writerIndex - readerIndex;
	}
}