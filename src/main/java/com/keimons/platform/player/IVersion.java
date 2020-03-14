package com.keimons.platform.player;

import java.util.function.BiConsumer;

/**
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IVersion<DataT extends IVersion<DataT>> {

	@SuppressWarnings("unchecked")
	default <ValueT> void change(BiConsumer<DataT, ValueT> consumer, ValueT value) {
		consumer.accept((DataT) this, value);
	}
}