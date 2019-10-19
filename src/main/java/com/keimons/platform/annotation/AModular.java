package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用模块
 * <p>
 * 包级注解，这个注解描述了一个模块的相关信息
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AModular {

	/**
	 * 模块名字
	 *
	 * @return 模块名字
	 */
	String Name();

	/**
	 * 模块加载优先级
	 *
	 * @return 加载优先级
	 */
	int Priority() default 1000;
}