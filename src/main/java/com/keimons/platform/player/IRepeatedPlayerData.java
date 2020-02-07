package com.keimons.platform.player;

import com.keimons.platform.iface.IRepeatedGameData;

/**
 * 可重复的标识
 *
 * @param <T> 数据主键类型
 * @author monkey1993
 * @version 1.0
 **/
public interface IRepeatedPlayerData<T> extends IPlayerData, IRepeatedGameData<T> {

}