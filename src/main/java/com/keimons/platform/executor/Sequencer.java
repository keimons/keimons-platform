package com.keimons.platform.executor;

import com.keimons.platform.unit.UnsafeUtil;
import jdk.internal.vm.annotation.ForceInline;
import sun.misc.Unsafe;

/**
 * 环形Buffer计数器
 *
 * 数据的存取应该是原子的操作，但是，使用环形Buffer实际上不论是存入还是取出，都会经过两步。
 * 存入过程：1.将数据存入环形Buffer中 2.修改存入位置标记
 * 取出过程：1.将数据从环形Buffer取出 2.修改取出位置标记
 * 为了保证整个过程是原子性的，所以我们将这个操作拆分为三步：
 * 1.根据已经写入的位置，生成标记位，标记成功的线程进行后续操作，标记失败的线程重复此操作，直到成功为止。
 * 2.将数据存入环形Buffer中
 * 3.修改已写入位置为标记位置
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
	private static final long READER_MARK_VALUE;

	/**
	 * 读取的字段
	 */
	private static final long READER_INDEX_VALUE;

	/**
	 * 写入的字段
	 */
	private static final long WRITER_MARK_VALUE;

	/**
	 * 写入的字段
	 */
	private static final long WRITER_INDEX_VALUE;

	static {
		long readerMark;
		try {
			readerMark = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("readerMark"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			readerMark = 0;
		}
		READER_MARK_VALUE = readerMark;

		long readerIndex;
		try {
			readerIndex = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("readerIndex"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			readerIndex = 0;
		}
		READER_INDEX_VALUE = readerIndex;

		long writerMark;
		try {
			writerMark = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("writerMark"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			writerMark = 0;
		}
		WRITER_MARK_VALUE = writerMark;

		long writerIndex;
		try {
			writerIndex = UNSAFE.objectFieldOffset(Sequencer.class.getDeclaredField("writerIndex"));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			writerIndex = 0;
		}
		WRITER_INDEX_VALUE = writerIndex;
	}

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
	private volatile int writerMark;

	/**
	 * 对外展示的写入位置
	 */
	private volatile int writerIndex;

	/**
	 * 读取的位置
	 */
	private volatile int readerMark;

	/**
	 * 对外展示的读取位置
	 */
	private volatile int readerIndex;

	public Sequencer(int bufferSize) {
		this.bufferSize = bufferSize;
		this.mask = bufferSize - 1;
	}

	/**
	 * 获取存入的位置
	 * <p>
	 * 需要保证标记存入位置-存入数据是一个原子性的操作
	 *
	 * @return 存入位置
	 */
	@ForceInline
	public int beforeNext() {
		for (; ; ) {
			int writerIndex = this.writerIndex;
			int writerMark = writerIndex + 1;
			// 计算环形Buffer的下一个下标是否越界
			if (writerMark - readerIndex > bufferSize) {
				// TODO 环形Buffer没有可写入的位置 需要补充策略
				return -1;
			} else {
				if (UNSAFE.compareAndSwapInt(this, WRITER_MARK_VALUE, writerIndex, writerMark)) {
					return writerIndex & mask;
				}
			}
		}
	}

	/**
	 * 获取存入的位置
	 * <p>
	 * 需要保证标记存入位置-存入数据是一个原子性的操作
	 */
	@ForceInline
	public void markNext() {
		UNSAFE.putIntVolatile(this, WRITER_INDEX_VALUE, writerMark);
	}

	/**
	 * 获取取出的位置
	 *
	 * @return 取出位置
	 */
	@ForceInline
	public int beforeTake() {
		for (; ; ) {
			int readerIndex = this.readerIndex;
			int readMark = readerIndex + 1;
			// 考虑到int的数值越界，所以，这里不能使用 nextIndex > writer
			if (readMark - writerIndex > 0) {
				// TODO 环形Buffer没有可读取的位置 需要补充策略
				return -1;
			} else {
				if (UNSAFE.compareAndSwapInt(this, READER_MARK_VALUE, readerIndex, readMark)) {
					return readerIndex & mask;
				}
			}
		}
	}

	/**
	 * 获取取出的位置
	 */
	@ForceInline
	public void markTake() {
//		readerIndex++;
		UNSAFE.putIntVolatile(this, READER_INDEX_VALUE, readerMark);
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
		return writerMark - readerMark;
	}
}