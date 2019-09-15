package com.keimons.platform.unit;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LimitLinkedList<E> extends LinkedList<E> {

	private Lock lock = new ReentrantLock();

	/**
	 * 限制长度
	 */
	private int limit;

	@Override
	public boolean add(E e) {
		lock.lock();
		try {
			super.add(e);
			if (super.size() > limit) {
				removeFirst();
			}
		} finally {
			lock.unlock();
		}
		return true;
	}
}