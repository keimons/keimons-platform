package com.keimons.nutshell.modular;

import com.keimons.nutshell.event.Event;
import com.keimons.nutshell.event.IEventCode;
import com.keimons.nutshell.event.IEventHandler;

/**
 * 业务逻辑管理
 * <p>
 * 在这里管理该模块的业务逻辑部分，为防止多重启继承，即便是标注了元注解：
 * {@link java.lang.annotation.Inherited}的接口，也不会将注解继承
 * 下去。为了便于开发，所以新增BaseService基类。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
@AService
public abstract class BaseService implements IEventHandler {

	@Override
	public IEventCode<?>[] register() {
		return new IEventCode[0];
	}

	@Override
	public void handler(Event event) {
	}
}