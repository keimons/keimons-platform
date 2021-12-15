package com.keimons.nutshell.asm;

/**
 * 动态修改枚举
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
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