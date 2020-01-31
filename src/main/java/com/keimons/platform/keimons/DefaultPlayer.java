package com.keimons.platform.keimons;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AGameData;
import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.iface.IGameData;
import com.keimons.platform.iface.ILoaded;
import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.module.BytesModuleSerialize;
import com.keimons.platform.module.IModule;
import com.keimons.platform.player.PlayerManager;
import com.keimons.platform.player.BasePlayer;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.session.Session;
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
 * 玩家类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultPlayer extends BasePlayer<String> {

	/**
	 * 数据是否已经加载
	 * <p>
	 * 为了防止数据被重复加载，所以需要一个标识符，标识数据是否已经被加载了。如果数据已经被
	 * 加载，则不会向这个{@code DefaultPlayer}中进行二次加载，以防止覆盖之前的对象。
	 */
	private boolean loaded;

	/**
	 * 客户端-服务器会话
	 * <p>
	 * 客户端和服务器相互绑定，向服务器发送数据通过客户端完成
	 */
	protected Session session;

	@Override
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public Runnable getLoader(Consumer<IPlayer<String>> consumer) {
		return () -> {
			if (this.isLoaded()) {
				LogService.warn("当前玩家已经加载过数据，正在重复加载：" + this.getIdentifier());
			}
			String identifier = this.getIdentifier();
			if (modules.size() == 0) {
				getLoader().accept(identifier);
			}
			if (consumer != null) {
				consumer.accept(this);
			}
			this.setLoaded(true);
		};
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
				int size = 0;
				Map<byte[], byte[]> moduleBytes = RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, identifier);
				if (moduleBytes != null) {
					for (Map.Entry<byte[], byte[]> entry : moduleBytes.entrySet()) {
						String moduleName = new String(entry.getKey(), Charset.forName("UTF-8"));
						Class<? extends IGameData> clazz = PlayerManager.classes.get(moduleName);
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
				checkModule();
				for (IModule<? extends IGameData> value : this.modules.values()) {
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