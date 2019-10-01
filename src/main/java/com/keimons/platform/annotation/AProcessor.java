package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息描述注解 服务器启动时会扫描所有实现这个注解类
 * <br />
 * 这里描述了消息处理器的相关信息，在{@link com.keimons.platform.process.IProcessor}定义了业务如何处理，
 * 两个模块独立工作，不会合二为一
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-20
 * @since 1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AProcessor {

	/**
	 * 协议号
	 *
	 * @return 消息的协议号
	 */
	int MsgCode();

	/**
	 * 协议接受的间隔时间
	 *
	 * @return 间隔时间
	 */
	int Interval() default 200;

	/**
	 * 协议描述
	 *
	 * @return 描述
	 */
	String Desc() default "没有描述";
}