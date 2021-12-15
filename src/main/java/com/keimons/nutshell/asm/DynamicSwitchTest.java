package com.keimons.nutshell.asm;

import com.keimons.nutshell.unit.DynamicEnumUtil;

/**
 * 动态{@code switch}测试
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class DynamicSwitchTest {

	public static void main(String[] args) throws Throwable {
		DynamicEnumUtil.addEnum(SwitchTestEnum.class, "DEBUG");
		SwitchTestEnum test = SwitchTestEnum.valueOf("TEST");

		SwitchTestEnum TEST = DynamicSwitch.valueOf(SwitchTestEnum.class, "TEST");
		SwitchTestEnum DEBUG = DynamicSwitch.valueOf(SwitchTestEnum.class, "DEBUG");
		SwitchTestEnum RUN = DynamicSwitch.valueOf(SwitchTestEnum.class, "RUN");
		System.out.println("查找完成：" + TEST);
		System.out.println("查找完成：" + DEBUG);
		System.out.println("查找完成：" + RUN);
	}
}