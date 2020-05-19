package com.keimons.platform.executor;

import com.keimons.platform.unit.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.function.Supplier;

/**
 * 环形Buffer
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-05-16
 * @since 1.8
 **/
public class RingBuffer<T> {

	/**
	 * 填充数据
	 * <p>
	 * 现在的64位计算机，会每次读取64个字节，另外，处理器的缓存行预取功能(Adjacent Cache-Line Prefetch)，
	 * 实际上是缓存了两个缓存行，所以需要填充左右各128个字节
	 */
	private static final int BUFFER_PAD;

	/**
	 * 计算在实际使用的真实位置
	 */
	private static final long REF_ARRAY_BASE;

	/**
	 * 引用类型占用字节数 开启指针压缩，每个引用占用4个字节，关闭指针压缩，每个引用占用8个字节
	 */
	private static final int REF_ELEMENT_SHIFT;

	/**
	 * Unsafe操作类
	 */
	private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();

	static {
		// 引用占用的字节数
		final int scale = UNSAFE.arrayIndexScale(Object[].class);
		if (4 == scale) {
			REF_ELEMENT_SHIFT = 2;
		} else if (8 == scale) {
			REF_ELEMENT_SHIFT = 3;
		} else {
			throw new IllegalStateException("Unknown pointer size");
		}
		// 计算填充元素数量
		BUFFER_PAD = 128 / scale;
		REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Node[].class) + (BUFFER_PAD << REF_ELEMENT_SHIFT);
	}

	/**
	 * 填充数据
	 */
	protected long p1, p2, p3, p4, p5, p6, p7;

	/**
	 * 环形数组，用于存放元素
	 */
	private final Object[] entries;

	/**
	 * 环形Buffer的大小
	 */
	protected final int bufferSize;

	/**
	 * 计数器
	 */
	protected final Sequencer sequencer;

	/**
	 * 填充数据
	 */
	protected long p8, p9, p10, p11, p12, p13, p14;

	public RingBuffer(int bufferSize) {
		if (bufferSize < 1) {
			throw new IllegalArgumentException("BufferSize must not be less than 1");
		}
		if (Integer.bitCount(bufferSize) != 1) {
			throw new IllegalArgumentException("Buffer Size must be a power of 2");
		}
		this.sequencer = new Sequencer(bufferSize);
		this.bufferSize = bufferSize;

		this.entries = new Object[bufferSize + 2 * BUFFER_PAD];
		fill(Node::new);
	}

	/**
	 * 填充数组
	 *
	 * @param factory 创建工厂
	 */
	private void fill(Supplier<Node<T>> factory) {
		for (int i = 0; i < bufferSize; i++) {
			entries[BUFFER_PAD + i] = factory.get();
		}
	}

	/**
	 * 环形Buffer中添加元素到队尾
	 *
	 * @param object 元素
	 * @return true.成功添加到队尾 false.已满
	 */
	@SuppressWarnings("unchecked")
	public boolean offer(T object) {
		int index = sequencer.beforeNext();
		if (index == -1) {
			return false;
		}
		Node<T> node = (Node<T>) UNSAFE.getObject(entries, REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT));
		node.setValue(object);
		sequencer.markNext();
		return true;
	}

	/**
	 * 环形Buffer中取出并移除一个元素
	 *
	 * @return 元素
	 */
	@SuppressWarnings("unchecked")
	public T take() {
		int index = sequencer.beforeTake();
		if (index == -1) {
			return null;
		}
		Node<T> node = (Node<T>) UNSAFE.getObject(entries, REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT));
		T result = node.setValue(null);
		sequencer.markTake();
		return result;
	}

	/**
	 * 节点
	 *
	 * @param <T> 节点中的数据类型
	 */
	private static class Node<T> {

		/**
		 * 值的变动需要线程是可见的
		 */
		private volatile T value;

		/**
		 * 获取节点中的值
		 *
		 * @return 节点中的值
		 */
		public T getValue() {
			return value;
		}

		/**
		 * 设置节点中的值
		 *
		 * @param value 节点中的值
		 * @return 原来的值
		 */
		public T setValue(T value) {
			try {
				return this.value;
			} finally {
				this.value = value;
			}
		}
	}
}