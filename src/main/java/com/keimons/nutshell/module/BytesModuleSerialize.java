package com.keimons.nutshell.module;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.keimons.nutshell.unit.JProtobufUtil;
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
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class BytesModuleSerialize implements IModuleSerializable<byte[]> {

	@Override
	@Nullable
	public byte[] serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException {
		if (coercive || module.isUpdate()) {
			BytesSerializeModule bsm = new BytesSerializeModule(module.size());
			for (IGameData data : module) {
				bsm.elements.add(JProtobufUtil.encode(data));
			}
			BytesSerializeEntity entity = new BytesSerializeEntity();
			entity.setCompress(module.isCompress());
			if (module.isCompress()) {
				entity.setValue(Snappy.compress(JProtobufUtil.encode(bsm)));
			} else {
				entity.setValue(JProtobufUtil.encode(bsm));
			}
			return JProtobufUtil.encode(entity);
		}
		return null;
	}

	@Override
	public <V extends IGameData> List<V> deserialize(Class<V> clazz, byte[] data) throws IOException {
		BytesSerializeEntity entity = JProtobufUtil.decode(BytesSerializeEntity.class, data);
		byte[] value;
		if (entity.compress) {
			value = Snappy.uncompress(entity.value);
		} else {
			value = entity.value;
		}
		BytesSerializeModule module = JProtobufUtil.decode(BytesSerializeModule.class, value);
		List<V> elements = new ArrayList<>(module.getElements().size());
		for (byte[] bytes : module.getElements()) {
			V item = JProtobufUtil.decode(clazz, bytes);
			if (item == null) {
				continue;
			}
			elements.add(item);
		}
		return elements;
	}

	static class BytesSerializeEntity implements ISerializeEntity {

		@Protobuf(order = 0, description = "是否压缩")
		private boolean compress;

		@Protobuf(order = 1, description = "存储数据")
		private byte[] value;

		public boolean isCompress() {
			return compress;
		}

		public void setCompress(boolean compress) {
			this.compress = compress;
		}

		public byte[] getValue() {
			return value;
		}

		public void setValue(byte[] value) {
			this.value = value;
		}
	}

	/**
	 * 序列化为byte字符串的模块
	 *
	 * @author houyn[monkey@keimons.com]
	 * @version 1.0
	 * @since 11
	 */
	static class BytesSerializeModule implements ISerializeModule<byte[]> {

		/**
		 * 模块数据
		 */
		@Protobuf(order = 0, description = "模块数据")
		private List<byte[]> elements;

		public BytesSerializeModule() {
			this.elements = new ArrayList<>();
		}

		public BytesSerializeModule(int size) {
			this.elements = new ArrayList<>(size);
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