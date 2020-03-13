package com.keimons.platform.keimons;

import com.keimons.platform.executor.KeimonsExecutor;
import com.keimons.platform.process.BaseHandlerManager;
import com.keimons.platform.session.Session;

/**
 * 默认实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultHandlerManager extends BaseHandlerManager<Session, Object> {

	public DefaultHandlerManager() {
		super(new KeimonsExecutor(DefaultExecutorEnum.class));
	}

	@Override
	protected int getMsgCode(Object message) {
		return 0;
	}
}