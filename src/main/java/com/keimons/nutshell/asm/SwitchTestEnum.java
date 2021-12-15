package com.keimons.nutshell.asm;

import java.lang.invoke.MethodHandle;

/**
 * 动态修改枚举
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public enum SwitchTestEnum {
	TEST, NAME;

	@SuppressWarnings("AutoSwitch")
	public static MethodHandle handle;

	public static SwitchTestEnum valueOf(int index) {
		return SwitchTestEnum.values()[index];
	}
}