package com.keimons.platform.annotation;

import java.lang.annotation.*;

/**
 * 定时任务 一旦被标注了定时任务，则会在启动的时候被夹在到内存中
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 **/
@Inherited
@Target({ElementType.TYPE}) // 用于类、枚举
@Retention(RetentionPolicy.RUNTIME) // 在运行时加载到Annotation到JVM中
public @interface AJob {

}