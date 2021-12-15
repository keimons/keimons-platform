package com.keimons.nutshell.module;

/**
 * 数据结构
 * <p>
 * 系统中设计了大量的空接口，用于规范一个数据究竟是如何定位的。数据结构接口是顶级接口，系统中的所有数据都需要
 * 实现这个接口，这是用于标注这个对象是数据的重要标识。
 * <p>
 * 同时，这是进行存储的最小单位，可以将这个存储单位进行单独存储，也可以将存储单位集合存储。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @see com.keimons.nutshell.unit.SerializeUtil 序列化工具
 * @see com.keimons.nutshell.module.IModuleSerializable 模块序列化方案
 * @since 11
 */
public interface IGameData extends ISerializable {

}