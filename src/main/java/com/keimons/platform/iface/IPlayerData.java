package com.keimons.platform.iface;

import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.module.IPersistence;

/**
 * 玩家数据模块
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
@APlayerData
public interface IPlayerData extends IModule, IData, ILoaded, IDataVersion, IPersistence {

}