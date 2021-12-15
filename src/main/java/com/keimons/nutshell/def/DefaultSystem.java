package com.keimons.nutshell.def;

import com.keimons.nutshell.module.BaseSystem;
import com.keimons.nutshell.module.IRepeatedModule;
import com.keimons.nutshell.module.IRepeatedSystemData;
import com.keimons.nutshell.module.ISingularSystemData;

/**
 * 默认系统模块
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class DefaultSystem extends BaseSystem {

	@Override
	protected <K> void addRepeatedData(IRepeatedSystemData<K> data) {
		String moduleName = findModuleName(data.getClass());
		IRepeatedModule<K, IRepeatedSystemData<K>> module = computeIfAbsent(moduleName, v -> new DefaultRepeatedModule<>());
		module.add(data);
	}

	@Override
	protected void addSingularData(ISingularSystemData data) {
		String moduleName = findModuleName(data.getClass());
		computeIfAbsent(moduleName, v -> new DefaultSingularModule<>(data));
	}
}