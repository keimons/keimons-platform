package com.keimons.nutshell.module;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON字符串持久化方案
 * <p>
 * 系统中提供的一种持久化方案
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class JsonModuleSerialize implements IModuleSerializable<String> {

	@Override
	@Nullable
	public String serialize(IModule<? extends IGameData> module, boolean coercive) throws IOException {
		if (coercive || module.isUpdate()) {
			JsonSerializeEntity entity = new JsonSerializeEntity();
			for (IGameData data : module) {
				entity.value.add(data);
			}
			return JSONObject.toJSONString(
					entity,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.IgnoreNonFieldGetter,
					SerializerFeature.WriteEnumUsingToString
			);
		}
		return null;
	}

	@Override
	public <V extends IGameData> List<V> deserialize(Class<V> clazz, String data) throws IOException {
		JsonSerializeEntity entity = JSONObject.parseObject(data, JsonSerializeEntity.class);
		List<V> elements = new ArrayList<>(entity.value.size());
		for (Object obj : entity.value) {
			@SuppressWarnings("unchecked")
			V item = (V) obj;
			elements.add(item);
		}
		return elements;
	}

	/**
	 * 依托于JSON实现的持久化容器
	 */
	static class JsonSerializeEntity implements ISerializeEntity {

		private JSONArray value = new JSONArray();

		public JSONArray getValue() {
			return value;
		}

		public void setValue(JSONArray value) {
			this.value = value;
		}
	}
}