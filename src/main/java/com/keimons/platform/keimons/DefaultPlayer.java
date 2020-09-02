package com.keimons.platform.keimons;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.player.*;
import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.log.LogService;
import com.keimons.platform.module.*;
import com.keimons.platform.unit.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.redisson.client.codec.ByteArrayCodec;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 玩家类的一个默认实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultPlayer extends BasePlayer<String> {

	public DefaultPlayer(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public void init() {

	}

	@Override
	public void loaded() {
		LogService.info("加载成功！id：" + this.identifier);
	}

	@Override
	public void online() {
		LogService.info("登录成功！id：" + this.identifier);
	}

	@Override
	public <K> void addRepeatedData(@NotNull IRepeatedPlayerData<K> data) {
		String moduleName = findModuleName(data.getClass());
		IRepeatedModule<K, IRepeatedPlayerData<K>> module = computeIfAbsent(moduleName, v -> new DefaultRepeatedModule<>());
		module.add(data);
	}

	@Override
	public void addSingularData(@NotNull ISingularPlayerData data) {
		String moduleName = findModuleName(data.getClass());
		computeIfAbsent(moduleName, v -> new DefaultSingularModule<>(data));
		moduleNames.add(moduleName);
	}

	@Override
	public void save(boolean coercive) {
		Map<byte[], byte[]> bytes = new HashMap<>(modules.size());
		for (Map.Entry<String, IModule<? extends IPlayerData>> entry : modules.entrySet()) {
			byte[] moduleName = entry.getKey().getBytes(StandardCharsets.UTF_8);
			try {
				byte[] serialize = SerializeUtil.serialize(
						entry.getValue(), coercive
				);
				if (serialize != null) {
					bytes.put(moduleName, serialize);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (bytes.size() > 0) {
			RedissonManager.setMapValues(ByteArrayCodec.INSTANCE, identifier, bytes);
		}
	}

	@Override
	public void load(Class<? extends IPlayerData>[] modules) {
		try {
			Set<byte[]> moduleNames = RedissonManager.getMapKeys(ByteArrayCodec.INSTANCE, identifier);
			for (byte[] moduleName : moduleNames) {
				this.moduleNames.add(new String(moduleName));
			}
			if (modules.length != 0) {
				moduleNames = new HashSet<>();
				for (Class<? extends IPlayerData> clazz : modules) {
					APlayerData annotation = clazz.getAnnotation(APlayerData.class);
					if (annotation == null) {
						continue;
					}
					moduleNames.add(annotation.moduleName().getBytes());
				}
			}
			int size = 0;
			if (moduleNames.size() > 0) {
				Map<byte[], byte[]> moduleBytes = RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, identifier, moduleNames);
				if (moduleBytes != null) {
					for (Map.Entry<byte[], byte[]> entry : moduleBytes.entrySet()) {
						String moduleName = new String(entry.getKey(), StandardCharsets.UTF_8);
						Class<? extends IPlayerData> clazz = PlayerManager.classes.get(moduleName);
						List<? extends IPlayerData> deserialize = SerializeUtil.deserialize(clazz, entry.getValue());
						for (IPlayerData playerData : deserialize) {
							if (playerData == null) {
								continue;
							}
							add(playerData);
						}
					}
				}
			}
			if (KeimonsServer.KeimonsConfig.isDebug()) {
				LogService.debug("玩家ID：" + identifier + "，数据模块共计：" + size + "字节！");
			}
		} catch (Exception e) {
			LogService.error(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void remove(Class<? extends IPlayerData>... classes) {
		for (Class<? extends IPlayerData> clazz : classes) {
			APlayerData annotation = clazz.getAnnotation(APlayerData.class);
			String moduleName = annotation.moduleName();
			RedissonManager.delMapValue(identifier, moduleName.getBytes());
		}
	}
}