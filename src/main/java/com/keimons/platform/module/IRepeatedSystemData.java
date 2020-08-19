package com.keimons.platform.module;

import com.keimons.platform.iface.IRepeatedData;

/**
 * 组织公共数据
 *
 * @param <T> 数据主键类型
 * @author monkey1993
 * @version 1.0
 * @since 11
 **/
public interface IRepeatedSystemData<T> extends ISystemData, IRepeatedData<T> {

}