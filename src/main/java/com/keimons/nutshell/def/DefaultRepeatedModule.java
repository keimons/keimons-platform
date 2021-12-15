package com.keimons.nutshell.def;

import com.keimons.nutshell.module.BaseRepeatedModule;
import com.keimons.nutshell.module.IGameData;
import com.keimons.nutshell.module.IRepeatedData;

/**
 * String作为Key的可重复的数据实现
 * <p>
 * 数据主键是一个String类型的数据
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class DefaultRepeatedModule<K, T extends IGameData & IRepeatedData<K>> extends BaseRepeatedModule<K, T> {

}