package com.keimons.platform.keimons;

import com.keimons.platform.iface.IRepeatedPlayerData;
import com.keimons.platform.iface.ISingularPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.module.BaseModules;
import com.keimons.platform.module.ModulesManager;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.session.Session;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 玩家类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class DefaultPlayer implements IPlayer<String> {

	/**
	 * 数据是否已经加载
	 * <p>
	 * 为了防止数据被重复加载，所以需要一个标识符，标识数据是否已经被加载了。如果数据已经被
	 * 加载，则不会向这个{@code DefaultPlayer}中进行二次加载，以防止覆盖之前的对象。
	 */
	private boolean loaded;

	/**
	 * 模块数据
	 * <p>
	 * 存储玩家所有的模块数据，玩家的数据存储在模块中。模块可以挂在多个{@code IPlayer}下，
	 * 但是，同一个模块在数据库中只有一份。
	 */
	protected DefaultModules modules;

	/**
	 * 客户端-服务器会话
	 * <p>
	 * 客户端和服务器相互绑定，向服务器发送数据通过客户端完成
	 */
	protected Session session;

	@Override
	public String uuid() {
		return modules.getIdentifier();
	}

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
	public void setModules(BaseModules<String> baseModules) {
		this.modules = (DefaultModules) baseModules;
	}

	@Override
	public <V extends ISingularPlayerData> V get(Class<V> clazz) {
		return modules.get(clazz);
	}

	@Override
	public <V extends IRepeatedPlayerData> V get(Class<V> clazz, Object dataId) {
		return modules.get(clazz, dataId);
	}

	@Override
	public <V extends IRepeatedPlayerData> V remove(Class<V> clazz, Object dataId) {
		return modules.remove(clazz, dataId);
	}

	@Override
	public DefaultModules getModules() {
		return modules;
	}

	@Override
	public Runnable getLoader(Consumer<IPlayer<String>> consumer, AtomicReference<BaseModules<String>> reference) {
		return () -> {
			if (this.isLoaded()) {
				LogService.warn("当前玩家已经加载过数据，正在重复加载：" + this.uuid());
			}
			String identifier = this.uuid();
			BaseModules<String> baseModules = ModulesManager.getModules(identifier);
			DefaultModules modules = (DefaultModules) baseModules;
			if (modules == null) {
				modules = new DefaultModules(identifier);
				modules.getLoader().accept(identifier);
			}
			if (consumer != null) {
				consumer.accept(this);
			}
			this.setLoaded(true);
			this.setModules(modules);
			if (reference != null) {
				reference.set(modules);
			}
		};
	}

	@Override
	public void setActiveTime(long activeTime) {
		modules.setActiveTime(activeTime);
	}

	@Override
	public long getActiveTime() {
		return modules.getActiveTime();
	}
}