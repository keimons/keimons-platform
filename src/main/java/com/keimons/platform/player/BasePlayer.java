package com.keimons.platform.player;

import com.keimons.platform.iface.IModule;
import com.keimons.platform.module.Modules;
import com.keimons.platform.session.Session;

import java.util.Map;

/**
 * 玩家基类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BasePlayer implements IPlayer {

	/**
	 * 数据是否已经加载
	 * <p>
	 * 为了防止数据被重复加载，所以需要一个标识符，标识数据是否已经被加载了。如果数据已经被
	 * 加载，则不会向这个{@code BasePlayer}中进行二次加载，以防止覆盖之前的对象。
	 */
	private boolean loaded;

	/**
	 * 模块数据
	 * <p>
	 * 存储玩家所有的模块数据，玩家的数据存储在模块中。模块可以挂在多个{@code IPlayer}下，
	 * 但是，同一个模块在数据库中只有一份。
	 */
	protected Modules modules;

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
	public void setModules(Modules modules) {
		this.modules = modules;
	}

	@Override
	public <T extends IModule> T getModule(Class<T> module) {
		return getModule(module, Modules.DEFAULT_MODULE_KEY);
	}

	@Override
	public <T extends IModule> T getModule(Class<T> module, Object object) {
		return modules.getModule(module, object);
	}

	@Override
	public Map<String, Map<Object, IModule>> getModules() {
		return modules.getModules();
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