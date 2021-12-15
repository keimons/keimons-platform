package com.keimons.nutshell.module;

import com.keimons.nutshell.player.IPlayerData;

/**
 * 可重复的标识
 *
 * @param <T> 数据主键类型
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public interface IRepeatedPlayerData<T> extends IPlayerData, IRepeatedData<T> {

}