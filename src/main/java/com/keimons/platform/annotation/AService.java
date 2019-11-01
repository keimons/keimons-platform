package com.keimons.platform.annotation;

import java.lang.annotation.*;

/**
 * 服务注解 服务器启动时会扫描所有实现这个注解类
 * <p>
 * 服务是负责处理玩家逻辑的，是组成一个功能的几个核心模块之一，我们会在服务中
 * 处理公共的逻辑，可以将公共的方法写在服务中，所有的服务将由Keimons框架进行
 * 缓存。{@link BaseService}服务需要继承自这个接口。或者继承自IService并
 * 自行标注AService注解。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AService {
}