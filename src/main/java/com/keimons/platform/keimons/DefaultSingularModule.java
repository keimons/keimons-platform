package com.keimons.platform.keimons;

import com.keimons.platform.module.BaseSingularModule;
import com.keimons.platform.module.IGameData;
import com.keimons.platform.module.ISingularData;

/**
 * 默认非重复数据实现
 *
 * @author monkey1993
 * @version 1.0
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