package com.keimons.platform.asm;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-03
 * @since 1.8
 **/
public class Test {

	public static SwitchTestEnum create(String name) {
		switch (name) {
			case "TEST":
				return SwitchTestEnum.TEST;
			case "NAME":
				return SwitchTestEnum.NAME;
			default:
				return null;
		}
	}

	public SwitchTestEnum test(int i) {
		return SwitchTestEnum.valueOf(i);
	}
}