package com.keimons.platform.player;

import java.lang.annotation.Annotation;

/**
 * 注解查找异常
 * <p>
 * {@code RuntimeException}运行时异常的一种。我们需要直接
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class AnnotationNotFoundException extends RuntimeException {

	/**
	 * 注解未找到异常
	 *
	 * @param clazz      要查找注解的类
	 * @param annotation 要查找的注解
	 */
	public AnnotationNotFoundException(Class<?> clazz, Class<? extends Annotation> annotation) {
		super("not found annotation " + annotation.getSimpleName() + " in class " + clazz.getName());
	}
}