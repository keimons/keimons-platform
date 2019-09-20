package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务注解 服务器启动时会扫描所有实现这个注解类
 *
 * @author monkey1993
 * @since 1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AService {

	/**
	 * 启动优先级
	 *
	 * @return 启动优先级
	 */
	int Priority();

	/**
	 * 功能名称
	 *
	 * @return 功能名称
	 */
	String Name() default "功能名字";

	/**
	 * 功能描述
	 *
	 * @return 功能描述
	 */
	String Desc() default "功能描述";
}