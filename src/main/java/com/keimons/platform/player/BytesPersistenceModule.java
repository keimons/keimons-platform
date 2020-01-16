package com.keimons.platform.player;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.keimons.platform.unit.CodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 字节数组持久化方案
 * <p>
 * 系统中提供的一种持久化方案
 *
 * @author monkey1993
 * @version 1.0
 * @date 2020-01-16
 **/
public class BytesPersistenceModule implements IPersistenceModule<byte[]> {

	@Protobuf(order = 0, description = "模块数据")
	public List<byte[]> module = new ArrayList<>();

	@Override
	public byte[] getData() {
		return CodeUtil.encode(this);
	}

	public List<byte[]> getModule() {
		return module;
	}

	public void setModule(List<byte[]> module) {
		this.module = module;
	}
}