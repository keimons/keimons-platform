package com.keimons.platform.keimons;

import com.keimons.platform.module.BaseRepeatedModule;
import com.keimons.platform.module.IGameData;
import com.keimons.platform.module.IRepeatedData;

/**
 * String作为Key的可重复的数据实现
 * <p>
 * 数据主键是一个String类型的数据
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 **/
public class DefaultRepeatedModule<K, T extends IGameData & IRepeatedData<K>> extends BaseRepeatedModule<K, T> {

}