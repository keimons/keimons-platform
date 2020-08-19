package com.keimons.platform.module;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.unit.JProtobufUtil;
import org.jetbrains.annotations.Nullable;
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
public class BytesModuleSerialize implements IModuleSerializable<byte[]> {

	/**
	 * 模块数据
	 */
	@Protobuf(order = 0, description = "模块数据")
	private byte[] bytes;

	/**
	 * 是否压缩
	 * <p>
	 * 为了标识数据是否被压缩过，所以，此数据时和存库数据平级关系，根据这个字段，{@link #bytes}
	 * 从数据库中读出的数据是否解压。
	 */
	@Protobuf(order = 1, description = "是否压缩")
	private boolean compress;

	@Override
	@Nullable
	public byte[] serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException {
		BytesSerializeModule serializable = new BytesSerializeModule();
		serializable.merge(module, coercive);
		if (serializable.getElements().size() <= 0) {
			return null;
		}
		bytes = JProtobufUtil.encode(serializable);
		compress = module.isCompress();
		if (compress) {
			bytes = Snappy.compress(bytes);
		}
		return JProtobufUtil.encode(this);
	}

	@Override
	public <V extends IGameData> List<V> deserialize(Class<V> clazz) throws IOException {
		if (compress) {
			bytes = Snappy.uncompress(bytes);
		}
		BytesSerializeModule serializable = JProtobufUtil.decode(BytesSerializeModule.class, bytes);
		List<V> elements = new ArrayList<>(serializable.getElements().size());
		for (byte[] bytes : serializable.getElements()) {
			V data = JProtobufUtil.decode(clazz, bytes);
			if (data == null) {
				continue;
			}
			elements.add(data);
		}
		return elements;
	}

	/**
	 * 序列化为byte字符串的模块
	 *
	 * @author monkey1993
	 * @version 1.0
	 */
	static class BytesSerializeModule implements ISerializeModule<byte[]> {

		/**
		 * 模块数据
		 */
		@Protobuf(order = 0, description = "模块数据")
		private List<byte[]> elements = new ArrayList<>();

		public void merge(IModule<? extends IGameData> module, boolean coercive) throws IOException {
			if (coercive || module.isUpdate()) {
				for (IGameData data : module) {
					byte[] persistence =  JProtobufUtil.encode(data);
					if (persistence != null) {
						this.elements.add(persistence);
					}
				}
			}
		}

		@Override
		public List<byte[]> getElements() {
			return elements;
		}

		public void setElements(List<byte[]> elements) {
			this.elements = elements;
		}
	}
}