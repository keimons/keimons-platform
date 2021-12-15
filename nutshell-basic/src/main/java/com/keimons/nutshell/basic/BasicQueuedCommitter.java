package com.keimons.nutshell.basic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息排队
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public abstract class BasicQueuedCommitter implements ICommitter {

	private static final VarHandle HEAD;
	private static final VarHandle TAIL;
	private static final VarHandle BUSY;
	private static final VarHandle NEXT;
	private static final VarHandle TASK;

	static {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			HEAD = lookup.findVarHandle(BasicQueuedCommitter.class, "head", Node.class);
			TAIL = lookup.findVarHandle(BasicQueuedCommitter.class, "tail", Node.class);
			BUSY = lookup.findVarHandle(BasicQueuedCommitter.class, "busy", int.class);
			NEXT = lookup.findVarHandle(Node.class, "next", Node.class);
			TASK = lookup.findVarHandle(Node.class, "task", Runnable.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private transient static final Node EMPTY = new Node();

	/**
	 * 队列头
	 */
	private transient volatile Node head;

	/**
	 * 队列尾
	 */
	private transient volatile Node tail;

	/**
	 * 提交者的状态
	 */
	private volatile int busy;

	@Override
	public void commit(int executorStrategy, int threadCode, Runnable task) {
		if (busy == 0 && BUSY.compareAndSet(this, 0, 1)) {
			if (isEmpty()) {
				commit0(executorStrategy, threadCode, task);
			} else {
				offer(executorStrategy, threadCode, task);
				poll();
			}
		} else {
			offer(executorStrategy, threadCode, task);
			commitNext();
		}
	}

	boolean isEmpty() {
		restartFromHead:
		for (; ; ) {
			for (Node h = head, p = h, q; ; p = q) {
				boolean hasItem = (p.task != null);
				if (hasItem || (q = p.next) == null) {
					updateHead(h, p);
					return (hasItem ? p : null) == null;
				} else if (p == q)
					continue restartFromHead;
			}
		}
	}

	public boolean offer(int executorStrategy, int threadCode, Runnable task) {
		final Node newNode = new Node(executorStrategy, threadCode, task);

		for (Node t = tail, p = t; ; ) {
			Node q = p.next;
			if (q == null) {
				// p is last node
				if (NEXT.compareAndSet(p, null, newNode)) {
					// Successful CAS is the linearization point
					// for e to become an element of this queue,
					// and for newNode to become "live".
					if (p != t) // hop two nodes at a time; failure is OK
						TAIL.weakCompareAndSet(this, t, newNode);
					return true;
				}
				// Lost CAS race to another thread; re-read next
			} else if (p == q)
				// We have fallen off list.  If tail is unchanged, it
				// will also be off-list, in which case we need to
				// jump to head, from which all live nodes are always
				// reachable.  Else the new tail is a better bet.
				p = (t != (t = tail)) ? t : head;
			else
				// Check for tail updates after two hops.
				p = (p != t && t != (t = tail)) ? t : q;
		}
	}

	public void poll() {
		restartFromHead:
		for (; ; ) {
			for (Node h = head, p = h, q = null; ; p = q) {
				final int executorStrategy = p.executorStrategy;
				final int threadCode = p.threadCode;
				final Runnable task = p.task;
				if (task != null && p.casItem(task, null)) {
					// Successful CAS is the linearization point
					// for item to be removed from this queue.
					if (p != h) // hop two nodes at a time
						updateHead(h, ((q = p.next) != null) ? q : p);
					commit0(executorStrategy, threadCode, task);
					return;
				} else if ((q = p.next) == null) {
					updateHead(h, p);
					return;
				} else if (p == q)
					continue restartFromHead;
			}
		}
	}

	public void commitNext() {
		if (BUSY.compareAndSet(this, 0, 1)) {
			poll();
		}
	}

	final void updateHead(Node h, Node p) {
		if (h != p && HEAD.compareAndSet(this, h, p))
			NEXT.setRelease(h, h);
	}

	abstract void commit0(int executorStrategy, int threadCode, Runnable task);

	static class Node {

		/**
		 * 后置节点
		 */
		volatile Node next;

		/**
		 * 执行策略
		 */
		final int executorStrategy;

		/**
		 * 线程码
		 * <p>
		 * 用于该任务由那个线程执行。
		 */
		final int threadCode;

		/**
		 * 任务
		 */
		Runnable task;

		Node() {
			executorStrategy = 0;
			threadCode = 0;
		}

		Node(int executorStrategy, int threadCode, Runnable task) {
			this.executorStrategy = executorStrategy;
			this.threadCode = threadCode;
			this.task = task;
		}

		boolean casItem(Runnable cmp, Runnable val) {
			// assert item == cmp || item == null;
			// assert cmp != null;
			// assert val == null;
			return TASK.compareAndSet(this, cmp, val);
		}
	}

	static ReentrantLock lock = new ReentrantLock(true);

	public static void main(String[] args) {
		Thread thread1 = new Thread(lock::lock, "thread1");
		thread1.start();
		Thread thread2 = new Thread(lock::lock, "thread2");
		thread2.start();
	}
}