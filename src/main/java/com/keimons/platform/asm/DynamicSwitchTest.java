package com.keimons.platform.asm;

import com.keimons.platform.unit.DynamicEnumUtil;

import static com.keimons.platform.asm.SwitchTestEnum.TEST;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2021-02-03
 * @since 1.8
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