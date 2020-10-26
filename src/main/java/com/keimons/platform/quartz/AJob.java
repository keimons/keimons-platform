package com.keimons.platform.quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 调度任务 一旦被标注了定时任务，则会在启动的时候被加载到内存中
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@Target({ElementType.TYPE}) // 用于类、枚举
@Retention(RetentionPolicy.RUNTIME) // 在运行时加载到Annotation到JVM中
public @interface AJob {

	/**
	 * 任务组
	 * <p>
	 * Quartz规定每一个任务都要有一个组名，通过任务组+任务名来确定一个唯一的任务，组名可以重复
	 *
	 * @return 任务组名
	 */
	String JobGroup() default "Keimons";

	/**
	 * 任务名
	 * <p>
	 * Quartz规定每一个任务都要有一个组名，通过任务组+任务名来确定一个唯一的任务，同一个分组下，组名唯一
	 *
	 * @return 任务名字
	 */
	String JobName();

	/**
	 * 任务执行表达式
	 *
	 * @return Cron表达式
	 */
	String JobCron();
}