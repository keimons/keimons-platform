package com.keimons.platform.annotation;

import java.lang.annotation.*;

/**
 * 数据模块 服务器启动时会扫描所有实现这个注解类
 * <p>
 * 数据模块是指玩家的数据模块，是组成一个功能的几个核心模块之一
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AModule {

}