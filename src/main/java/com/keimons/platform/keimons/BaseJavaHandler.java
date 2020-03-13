package com.keimons.platform.keimons;

import com.keimons.platform.process.IHandler;
import com.keimons.platform.session.ISession;

/**
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BaseJavaHandler<SessionT extends ISession, MessageT> implements IHandler<SessionT, MessageT> {

}