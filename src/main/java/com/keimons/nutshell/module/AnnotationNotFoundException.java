package com.keimons.nutshell.module;

import java.lang.annotation.Annotation;

/**
 * 注解查找失败异常
 * <p>
 * {@code RuntimeException}运行时异常的一种。我们需要直接
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class AnnotationNotFoundException extends RuntimeException {

	/**
	 * 注解查找失败异常
	 *
	 * @param clazz      要查找注解的类
	 * @param annotation 要查找的注解
	 */
	public AnnotationNotFoundException(Class<?> clazz, Class<? extends Annotation> annotation) {
		super("not found annotation " + annotation.getSimpleName() + " in class " + clazz.getName());
	}
}