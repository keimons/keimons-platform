package com.keimons.platform.keimons;

import com.keimons.platform.module.*;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-09-02
 * @since 1.8
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
