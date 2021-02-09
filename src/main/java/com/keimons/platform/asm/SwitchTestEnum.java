package com.keimons.platform.asm;

import java.lang.invoke.MethodHandle;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-04
 * @since 1.8
 **/
public enum SwitchTestEnum {
	TEST, NAME;

	@SuppressWarnings("AutoSwitch")
	public static MethodHandle handle;

	public static SwitchTestEnum valueOf(int index) {
		return SwitchTestEnum.values()[index];
	}
}