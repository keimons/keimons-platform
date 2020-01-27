package com.keimons.platform.keimons;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.iface.ILoaded;
import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.module.BaseModules;
import com.keimons.platform.module.BytesModuleSerialize;
import com.keimons.platform.module.IModule;
import com.keimons.platform.module.ModulesManager;
import com.keimons.platform.unit.CodeUtil;
import com.keimons.platform.unit.SerializeUtil;
import org.redisson.client.codec.ByteArrayCodec;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 玩家所有模块数据
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultModules extends BaseModules<String> {

	/**
	 * 构造函数
	 *
	 * @param identifier 数据唯一表示
	 */
	public DefaultModules(String identifier) {
		super(identifier);
	}

	@Override
	public void addRepeatedData(IRepeatedPlayerData data) {
		AGameData annotation = data.getClass().getAnnotation(AGameData.class);
		String moduleName = annotation.moduleName();
		DefaultRepeatedModule<IRepeatedPlayerData> module = computeIfAbsent(moduleName, v -> new DefaultRepeatedModule<>());
		module.add(data);
	}

	@Override
	public void addSingularData(ISingularPlayerData data) {
		AGameData annotation = data.getClass().getAnnotation(AGameData.class);
		String moduleName = annotation.moduleName();
		computeIfAbsent(moduleName, v -> new DefaultSingularModule<>(data));
	}

	@Override
	public void save(boolean coercive) {
		Map<byte[], byte[]> bytes = new HashMap<>(modules.size());
		for (Map.Entry<String, IModule<? extends IGameData>> entry : modules.entrySet()) {
			byte[] moduleName = entry.getKey().getBytes(Charset.forName("UTF-8"));
			try {
				byte[] serialize = SerializeUtil.serialize(BytesModuleSerialize.class, entry.getValue(), coercive);
				bytes.put(moduleName, serialize);
			} catch (IOException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		if (bytes.size() > 0) {
			RedissonManager.setMapValues(ByteArrayCodec.INSTANCE, identifier, bytes);
		}
	}

	@Override
	public Consumer<String> getLoader() {
		return (identifier) -> {
			try {
				BaseModules<String> baseModules = ModulesManager.getModules(identifier);
				DefaultModules modules = (DefaultModules) baseModules;
				if (modules != null) {
					ModulesManager.cacheModules(modules);
				}
				modules = new DefaultModules(identifier);
				int size = 0;
				Map<byte[], byte[]> moduleBytes = RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, identifier);
				if (moduleBytes != null) {
					for (Map.Entry<byte[], byte[]> entry : moduleBytes.entrySet()) {
						String moduleName = new String(entry.getKey(), Charset.forName("UTF-8"));
						Class<? extends IGameData> clazz = ModulesManager.classes.get(moduleName);
						BytesModuleSerialize serialize = CodeUtil.decode(BytesModuleSerialize.class, entry.getValue());

						List<? extends IGameData> deserialize = SerializeUtil.deserialize(serialize, clazz);
						for (IGameData playerData : deserialize) {
							if (playerData == null) {
								continue;
							}
							if (playerData instanceof IRepeatedPlayerData) {
								addRepeatedData((IRepeatedPlayerData) playerData);
							}
							if (playerData instanceof ISingularPlayerData) {
								addSingularData((ISingularPlayerData) playerData);
							}
						}
					}
				}
				if (KeimonsServer.KeimonsConfig.isDebug()) {
					LogService.debug("玩家ID：" + identifier + "，数据模块共计：" + size + "字节！");
				}
				modules.checkModule();
				for (IModule<? extends IGameData> value : modules.getModules().values()) {
					for (IGameData module : value.toCollection()) {
						if (module instanceof ILoaded) {
							((ILoaded) module).loaded(this);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}