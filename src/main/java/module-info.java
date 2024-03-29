module nutshell {
	requires nutshell.basic;
	requires jdk.unsupported;
	requires java.base;
	requires redisson;
	requires fastjson;
	requires disruptor;
	requires io.netty.all;
	requires mina.core;
	requires jprotobuf;
	requires com.google.protobuf;
	requires snappy.java;
	requires com.google.common;
	requires java.logging;
	requires slf4j.api;
	requires org.jetbrains.annotations;
	requires quartz;
	requires org.codehaus.groovy;
	requires org.objectweb.asm;
	exports com.keimons.nutshell.asm;
}