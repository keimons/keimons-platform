package com.keimons.platform.module;

import com.keimons.platform.iface.IRepeatedGameData;
import com.keimons.platform.player.IPlayerData;

/**
 * 可重复的标识
 *
 * @param <T> 数据主键类型
 * @author monkey1993
 * @version 1.0
 **/
public interface IRepeatedPlayerData<T> extends IPlayerData, IRepeatedGameData<T> {

}