package com.keimons.platform.annotation;

import java.lang.annotation.*;

/**
 * 玩家数据模块 服务器启动时会扫描所有实现这个注解类
 * <p>
 * 玩家数据模块是组成一个功能的八个核心模块之一，玩家总共有两个数据域，一个是玩家私有数据，一个是玩家公共数据。
 * 当玩家创建时，会反射创造出玩家的数据对象，并存在玩家身上
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 **/
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface APlayerData {

}