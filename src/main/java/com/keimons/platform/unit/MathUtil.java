package com.keimons.platform.unit;

public class MathUtil {

	public static int limit(int number, int limitMin, int limitMax) {
		if (limitMin > limitMax) {
			throw new NumberFormatException("");
		}
		if (number > limitMax) {
			return limitMax;
		}
		if (number < limitMin) {
			return limitMin;
		}
		return number;
	}
}