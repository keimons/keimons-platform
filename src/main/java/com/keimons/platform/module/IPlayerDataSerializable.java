package com.keimons.platform.module;

import com.keimons.platform.iface.ISerializable;

import java.io.IOException;

/**
 * 玩家数据序列化接口
 * <p>
 * 这是对于 {@link ISerializable} 的一个补充，系统设定中，允许玩家临时数据的存在，临时数据是不需要
 * 进行持久化的，此类数据无须实现 {@link IPlayerDataSerializable} 接口。一旦该类实现了这个接口，
 * 那么系统对于玩家序列化时，便会将这个对象序列化。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IPlayerDataSerializable extends ISerializable {

	/**
	 * 获取持久化数据
	 *
	 * @param notnull 是否强制获取数据
	 * @param <T>     序列化类型
	 * @return 最新数据{@code null}则表示无最新数据
	 * @throws IOException 序列化错误
	 */
	<T> T serialize(boolean notnull) throws IOException;
}