package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理注解 服务器启动时会扫描所有实现这个注解类
 *
 * @author monkey1993
 * @since 1.8
 */
@Target({ElementType.TYPE})   // 用于类、枚举
@Retention(RetentionPolicy.RUNTIME) // 在运行时加载到Annotation到JVM中
public @interface AManager {

	/**
	 * 启动优先级
	 *
	 * @return 启动优先级
	 */
	int Priority();

	/**
	 * 功能名称
	 *
	 * @return 模块名称
	 */
	String Name() default "功能名字";

	/**
	 * 功能描述
	 *
	 * @return 模块描述
	 */
	String Desc() default "功能描述";
}