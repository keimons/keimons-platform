package com.keimons.nutshell.def;

import com.keimons.nutshell.module.BaseSingularModule;
import com.keimons.nutshell.module.IGameData;
import com.keimons.nutshell.module.ISingularData;

/**
 * 默认非重复数据实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class DefaultSingularModule<T extends IGameData & ISingularData> extends BaseSingularModule<T> {

	/**
	 * 构造方法
	 *
	 * @param singular 数据
	 */
	DefaultSingularModule(T singular) {
		super(singular);
	}
}