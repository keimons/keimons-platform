package com.keimons.nutshell.player;

import com.keimons.nutshell.module.IDataVersion;

import java.util.function.BiConsumer;

/**
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IBanSetterVersion extends IDataVersion {

	@SuppressWarnings("unchecked")
	default <DataT extends IBanSetterVersion, Value1T> void change(BiConsumer<DataT, Value1T> consumer, Value1T value1) {
		consumer.accept((DataT) this, value1);
	}

	@SuppressWarnings("unchecked")
	default <DataT extends IBanSetterVersion, Value1T> void change(BiConsumer<DataT, Long> consumer, int value1) {
		consumer.accept((DataT) this, (long) value1);
	}
	/*
	@SuppressWarnings("unchecked")
	default <DataT extends IBanSetterVersion, Value1T, Value2T> void change(Consumer2<DataT, Value1T, Value2T> consumer, Value1T value1, Value2T value2) {
		consumer.accept((DataT) this, value1, value2);
	}

	@SuppressWarnings("unchecked")
	default <DataT extends IBanSetterVersion, Value1T, Value2T, Value3T> void change(Consumer3<DataT, Value1T, Value2T, Value3T> consumer, Value1T value1, Value2T value2, Value3T value3) {
		consumer.accept((DataT) this, value1, value2, value3);
	}

	@SuppressWarnings("unchecked")
	default <DataT extends IBanSetterVersion, Value1T, Value2T, Value3T, Value4T> void change(Consumer4<DataT, Value1T, Value2T, Value3T, Value4T> consumer, Value1T value1, Value2T value2, Value3T value3, Value4T value4) {
		consumer.accept((DataT) this, value1, value2, value3, value4);
	}


	interface Consumer1<DataT, ParamsT1> {

		void accept(DataT t, ParamsT1 u1);
	}

	interface Consumer2<DataT, ParamsT1, ParamsT2> {

		void accept(DataT t, ParamsT1 u1, ParamsT2 u2);
	}

	interface Consumer3<DataT, ParamsT1, ParamsT2, ParamsT3> {

		void accept(DataT t, ParamsT1 u1, ParamsT2 u2, ParamsT3 u3);
	}

	interface Consumer4<DataT, ParamsT1, ParamsT2, ParamsT3, ParamsT4> {

		void accept(DataT t, ParamsT1 u1, ParamsT2 u2, ParamsT3 u3, ParamsT4 u4);
	}
	 */
}