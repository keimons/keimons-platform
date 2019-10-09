package com.keimons.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 协议注解
 * <p>
 * 标注了这个类是用来处理客户端请求的，在系统启动时，系统会扫描所有的系统模块，当模块
 * 中的某个类标注了这个注解，则会加载到消息处理器中，这里描述了消息处理器的相关信息，
 * 在{@link com.keimons.platform.process.IProcessor}定义了业务如何处理，两
 * 个模块独立工作，不会合二为一
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AProcessor {

	/**
	 * 协议号
	 * <p>
	 * 服务器处理逻辑的单位，所有玩家行为均通过协议实现。这是协
	 * 议的唯一表示，客户端通过发送协议和数据的方式，表明客户端
	 * 要"做什么"。
	 *
	 * @return 消息的协议号
	 */
	int MsgCode();

	/**
	 * 协议接受的间隔时间
	 * <p>
	 * 服务器中防止客户端频繁的消息请求，每个消息都有接收间隔，
	 * 如果没有间隔代表该消息可以无任何限制的请求。
	 *
	 * @return 间隔时间
	 */
	int Interval() default 200;

	/**
	 * 协议描述
	 * <p>
	 * 描述玩家要"做什么"，当且仅当在Debug模式下，会输出所有正
	 * 在请求的协议信息，正式环境不会打印这个协议的描述
	 *
	 * @return 描述
	 */
	String Desc() default "没有描述";
}