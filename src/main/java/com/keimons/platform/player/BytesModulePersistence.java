package com.keimons.platform.player;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.iface.IData;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.iface.IRepeatedData;
import com.keimons.platform.module.ISerializable;
import com.keimons.platform.module.IModulePersistence;
import com.keimons.platform.unit.CodeUtil;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 字节数组持久化方案
 * <p>
 * 系统中提供的一种持久化方案
 *
 * @author monkey1993
 * @version 1.0
 **/
public class BytesModulePersistence implements IModulePersistence<byte[]> {

	/**
	 * 模块数据
	 */
	@Protobuf(order = 0, description = "模块数据")
	private List<byte[]> bytes = new ArrayList<>();

	/**
	 * 是否压缩
	 */
	@Protobuf(order = 1, description = "是否压缩")
	private boolean compress;

	@Override
	public byte[] serialize() throws IOException {
		return CodeUtil.encode(this);
	}

	@Override
	public void setModule(IModule<? extends IPlayerData> module, boolean coercive) throws IOException {
		if (module instanceof IRepeatedData) {
			coercive = true;
		}
		for (IData data : module.getPlayerData()) {
			if (data instanceof ISerializable) {
				ISerializable serializable = (ISerializable) data;
				APlayerData annotation = data.getClass().getAnnotation(APlayerData.class);
				this.compress = annotation.isCompress();
				byte[] persistence = serializable.serialize(coercive);
				if (annotation.isCompress()) {
					persistence = Snappy.compress(persistence);
				}
				if (persistence != null) {
					this.bytes.add(persistence);
				}
			}
		}
	}

	/**
	 * 序列化模块
	 *
	 * @param module   模块
	 * @param coercive 是否强制序列化
	 * @return 序列化后的数据
	 * @throws IOException 序列化异常
	 */
	public static byte[] serialize(IModule<? extends IPlayerData> module, boolean coercive) throws IOException {
		BytesModulePersistence persistence = new BytesModulePersistence();
		persistence.setModule(module, coercive);
		return persistence.serialize();
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public List<byte[]> getBytes() {
		return bytes;
	}

	public void setBytes(List<byte[]> bytes) {
		this.bytes = bytes;
	}
}