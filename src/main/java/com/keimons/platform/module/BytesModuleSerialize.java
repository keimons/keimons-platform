package com.keimons.platform.module;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.keimons.platform.annotation.APlayerData;
import com.keimons.platform.iface.IGameData;
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
	public byte[] serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException {
		BytesSerializeModule serializable = new BytesSerializeModule();
		serializable.serialize(module, coercive);
		bytes = CodeUtil.encode(serializable);
		compress = serializable.isCompress();
		if (compress) {
			bytes = Snappy.compress(bytes);
		}
		return CodeUtil.encode(this);
	}

	@Override
	public <V extends IGameData> List<V> deserialize(Class<V> clazz) throws IOException {
		if (compress) {
			bytes = Snappy.uncompress(bytes);
		}
		BytesSerializeModule serializable = CodeUtil.decode(BytesSerializeModule.class, bytes);
		List<V> elements = new ArrayList<>(serializable.getElements().size());
		for (byte[] bytes : serializable.getElements()) {
			V data = CodeUtil.decode(clazz, bytes);
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

		/**
		 * 数据是否压缩
		 * <p>
		 * 这个字段仅仅表明该模块是否需要进行压缩。这个字段和 {@link #elements} 平级，所以，这个字段如果写入数据库中，
		 * 是无法在解压之前读出该字段的。
		 */
		private transient boolean compress;

		@Override
		public void serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException {
			if (module instanceof IRepeatedPlayerData) {
				coercive = true;
			}
			for (IGameData data : module) {
				if (data instanceof IGameDataSerialize) {
					IGameDataSerialize serializable = (IGameDataSerialize) data;
					APlayerData annotation = data.getClass().getAnnotation(APlayerData.class);
					compress = annotation.isCompress();
					byte[] persistence = serializable.serialize(coercive);
					if (persistence != null) {
						this.elements.add(persistence);
					}
				}
			}
		}

		@Override
		public boolean isCompress() {
			return compress;
		}

		public void setCompress(boolean compress) {
			this.compress = compress;
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