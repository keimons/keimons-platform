package com.keimons.platform.unit;

import java.io.*;

/**
 * 自定义枚举基类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BaseEnum<E extends BaseEnum<E>>
		implements Comparable<E>, Serializable {

	private final String name;

	public final String name() {
		return name;
	}

	private final int ordinal;

	public final int ordinal() {
		return ordinal;
	}

	protected BaseEnum(String name, int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
	}

	public String toString() {
		return name;
	}

	public final boolean equals(Object other) {
		return this == other;
	}

	public final int hashCode() {
		return super.hashCode();
	}

	protected final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public final int compareTo(E o) {
		BaseEnum<?> other = o;
		BaseEnum<E> self = this;
		if (self.getClass() != other.getClass() && // optimization
				self.getDeclaringClass() != other.getDeclaringClass())
			throw new ClassCastException();
		return self.ordinal - other.ordinal;
	}

	@SuppressWarnings("unchecked")
	public final Class<E> getDeclaringClass() {
		Class<?> clazz = getClass();
		Class<?> zuper = clazz.getSuperclass();
		return (zuper == java.lang.Enum.class) ? (Class<E>) clazz : (Class<E>) zuper;
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		throw new InvalidObjectException("can't deserialize enum");
	}

	private void readObjectNoData() throws ObjectStreamException {
		throw new InvalidObjectException("can't deserialize enum");
	}
}