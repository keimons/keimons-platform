package com.keimons.platform.modular;

import com.keimons.platform.event.Event;
import com.keimons.platform.event.IEventCode;
import com.keimons.platform.event.IEventHandler;

/**
 * 业务逻辑管理
 * <p>
 * 在这里管理该模块的业务逻辑部分，为防止多重启继承，即便是标注了元注解：
 * {@link java.lang.annotation.Inherited}的接口，也不会将注解继承
 * 下去。为了便于开发，所以新增BaseService基类。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
@AService
public abstract class BaseService implements IService, IEventHandler {

	@Override
	public IEventCode<?>[] register() {
		return new IEventCode[0];
	}

	@Override
	public void handler(Event event) {
	}

	@Override
	public boolean start() {
		return false;
	}

	@Override
	public boolean shutdown() {
		return false;
	}
}