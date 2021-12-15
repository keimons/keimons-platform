package com.keimons.nutshell.module;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * 单数据实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class BaseSingularModule<T extends IGameData & ISingularData> implements ISingularModule<T> {

	/**
	 * 玩家数据
	 */
	protected final T singular;

	/**
	 * 构造方法
	 *
	 * @param singular 数据
	 */
	public BaseSingularModule(T singular) {
		this.singular = singular;
	}

	@Override
	public T get() {
		return singular;
	}

	@Override
	public void upgrade(int before, int current) {

	}

	@Override
	public Collection<T> values() {
		return Collections.singleton(singular);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(singular);
	}

	@Override
	public Spliterator<T> spliterator() {
		return singletonSpliterator(singular);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return singletonIterator(singular);
	}

	static <T> Spliterator<T> singletonSpliterator(final T element) {
		return new Spliterator<>() {
			long est = 1;

			@Override
			public Spliterator<T> trySplit() {
				return null;
			}

			@Override
			public boolean tryAdvance(Consumer<? super T> consumer) {
				Objects.requireNonNull(consumer);
				if (est > 0) {
					est--;
					consumer.accept(element);
					return true;
				}
				return false;
			}

			@Override
			public void forEachRemaining(Consumer<? super T> consumer) {
				tryAdvance(consumer);
			}

			@Override
			public long estimateSize() {
				return est;
			}

			@Override
			public int characteristics() {
				int value = (element != null) ? Spliterator.NONNULL : 0;

				return value | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE |
						Spliterator.DISTINCT | Spliterator.ORDERED;
			}
		};
	}

	/**
	 * 返回一个单例的迭代器
	 *
	 * @param e   唯一元素
	 * @param <E> 元素类型
	 * @return 迭代器
	 */
	static <E> Iterator<E> singletonIterator(final E e) {
		return new Iterator<>() {
			private boolean hasNext = true;

			public boolean hasNext() {
				return hasNext;
			}

			public E next() {
				if (hasNext) {
					hasNext = false;
					return e;
				}
				throw new NoSuchElementException();
			}
		};
	}
}