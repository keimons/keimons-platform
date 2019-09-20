package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息注解 服务器启动时会扫描所有实现这个注解类
 *
 * @author monkey1993
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