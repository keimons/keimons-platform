package com.keimons.platform.module;

import java.lang.annotation.*;

/**
 * 系统数据模块
 * <p>
 * 系统中的数据需要标注为系统数据模块
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASystemData {

	/**
	 * 模块名称
	 * <p>
	 * 系统中用来标识模块的名称的
	 *
	 * @return 模块名称
	 */
	String moduleName();

	/**
	 * 是否压缩
	 * <p>
	 * 该模块是否要对数据进行压缩
	 *
	 * @return 是否压缩
	 */
	boolean isCompress() default false;
}