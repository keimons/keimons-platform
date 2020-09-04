package com.keimons.platform.player;

import com.keimons.platform.module.*;
import com.keimons.platform.session.ISession;
import com.keimons.platform.unit.TimeUtil;
import jdk.internal.vm.annotation.ForceInline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 玩家基类
 *
 * @param <T> 玩家标识类型
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public abstract class BasePlayer<T> implements IPlayer<T> {

	/**
	 * 数据唯一标识
	 * <p>
	 * 当从数据库取出数据时，需要使用唯一标识符获取数据，当对数据进行持久化时，需要使用唯一标识符作为
	 * 主键，将数据存入数据库。
	 */
	protected T identifier;

	/**
	 * 数据是否已经加载
	 * <p>
	 * 为了防止数据被重复加载，所以需要一个标识符，标识数据是否已经被加载了。如果数据已经被
	 * 加载，则不会向这个{@code DefaultPlayer}中进行二次加载，以防止覆盖之前的对象。
	 */
	private boolean loaded;

	/**
	 * 玩家数据 Key:数据名称 Value:数据
	 */
	protected final ConcurrentHashMap<String, IModule<? extends IPlayerData>> modules = new ConcurrentHashMap<>();

	/**
	 * 已经初始化过的模块名称
	 * <p>
	 * 系统允许只加载用户的一部分数据。{@link #load(Class[])}加载数据时，如果数据没有完全加载，{@link #get(Class)}获取数据时，应该获取到的是
	 * {@code null}，如果该模块未曾初始化，那么则可以对该模块进行初始化。
	 * <p>
	 * 警告：如果模块已经初始化，再次初始化模块，会导致数据被覆盖。
	 */
	protected final Set<String> moduleNames = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

	/**
	 * 最后活跃时间
	 */
	protected volatile long activeTime = TimeUtil.currentTimeMillis();

	/**
	 * 客户端-服务器会话
	 * <p>
	 * 客户端和服务器相互绑定，向服务器发送数据通过客户端完成
	 */
	protected ISession session;

	/**
	 * 默认构造函数
	 */
	public BasePlayer() {
	}

	/**
	 * 构造函数
	 *
	 * @param identifier 数据唯一表示
	 */
	public BasePlayer(T identifier) {
		this.identifier = identifier;
	}

	/**
	 * 获取玩家的一个模块
	 * <p>
	 * 如果玩家已经被完全加载，如果缺少这个模块，则补充这个模块，如果玩家没有被完全加载，则不能
	 *
	 * @param clazz 模块名称
	 * @param <V>   玩家数据类型
	 * @return 数据模块
	 */
	@Override
	@NotNull
	@ForceInline
	public <V extends ISingularPlayerData> V get(@NotNull Class<V> clazz) {
		ISingularModule<V> module = findModule(clazz);
		if (module == null) {
			// inline
			module = createSingularModule(clazz);
		}
		return module.get();
	}

	/**
	 * 创建一个模块
	 *
	 * @param clazz 模块类型
	 * @param <V>   数据类型
	 * @return 模块
	 */
	@NotNull
	private <V extends ISingularPlayerData> ISingularModule<V> createSingularModule(Class<V> clazz) {
		String moduleName = findModuleName(clazz);
		synchronized (this) {
			if (!modules.containsKey(moduleName)) {
				try {
					var constructor = clazz.getDeclaredConstructor();
					var data = constructor.newInstance();
					data.init(this);
					addSingularData(data);
					moduleNames.add(moduleName);
				} catch (Exception e) {
					throw new ModuleCreateFailException(clazz, e);
				}
			}
		}
		return Objects.requireNonNull(findModule(clazz));
	}

	@Override
	@ForceInline
	public void add(IRepeatedPlayerData<?> data) {
		addRepeatedData(data);
	}

	@Override
	@Nullable
	@ForceInline
	public <K, V extends IRepeatedPlayerData<K>> V get(@NotNull Class<V> clazz, @NotNull K dataId) {
		IRepeatedModule<K, V> module = findModule(clazz);
		if (module == null) {
			return null;
		}
		return module.get(dataId);
	}

	/**
	 * 移除玩家的一个模块
	 *
	 * @param clazz  模块名称
	 * @param dataId 数据唯一IDs
	 * @param <V>    模块类型
	 * @return 数据 {@code null} 表示未找到这个模块或数据
	 */
	@Override
	@Nullable
	@ForceInline
	public <K, V extends IRepeatedPlayerData<K>> V remove(@NotNull Class<V> clazz, @NotNull K dataId) {
		IRepeatedModule<K, V> module = findModule(clazz);
		if (module == null) {
			return null;
		}
		return module.remove(dataId);
	}

	/**
	 * 查找模块的模块名称
	 *
	 * @param clazz 模块
	 * @param <V>   模块数据类型
	 * @return 模块名称
	 * @throws AnnotationNotFoundException 注解查找失败异常
	 */
	@ForceInline
	public <V extends IPlayerData> String findModuleName(@NotNull Class<V> clazz) {
		APlayerData annotation = clazz.getAnnotation(APlayerData.class);
		if (annotation == null) {
			throw new AnnotationNotFoundException(clazz, APlayerData.class);
		}
		return annotation.moduleName();
	}

	/**
	 * 查找一个模块
	 *
	 * @param clazz 数据类型
	 * @param <V>   数据类型
	 * @param <R>   模块类型
	 * @return 模块 {@code null} 模块不存在
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	@ForceInline
	public <V extends IPlayerData, R extends IModule<V>> R findModule(@NotNull Class<V> clazz) {
		String moduleName = findModuleName(clazz);
		return (R) modules.get(moduleName);
	}

	/**
	 * 获取数据模块
	 *
	 * @param moduleName 模块名字
	 * @param function   新建模块
	 * @param <V>        模块类型
	 * @return 模块
	 */
	@SuppressWarnings("unchecked")
	protected <V extends IModule<? extends IPlayerData>> V computeIfAbsent(
			String moduleName, Function<String, V> function) {
		Objects.requireNonNull(function);
		return (V) modules.computeIfAbsent(moduleName, function);
	}

	/**
	 * 增加一个可重复的模块数据
	 * <p>
	 * (线程安全的实现)
	 *
	 * @param data 数据
	 * @throws AnnotationNotFoundException 注解查找失败异常。
	 */
	@ForceInline
	protected abstract <K> void addRepeatedData(@NotNull IRepeatedPlayerData<K> data);

	/**
	 * 增加一个非重复的模块数据
	 *
	 * @param data 数据
	 * @throws AnnotationNotFoundException 注解查找失败异常。
	 */
	@ForceInline
	protected abstract void addSingularData(@NotNull ISingularPlayerData data);

	@Override
	public boolean hasModules(Class<? extends IPlayerData>[] classes) {
		for (Class<? extends IPlayerData> clazz : classes) {
			if (!modules.containsKey(findModuleName(clazz))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void unload(Class<? extends IPlayerData>[] classes) {
		for (Class<? extends IPlayerData> clazz : classes) {
			String moduleName = findModuleName(clazz);
			this.modules.remove(moduleName);
			this.moduleNames.remove(moduleName);
		}
	}

	@Override
	public void unloadIfNot(Class<? extends IPlayerData>[] classes) {
		Set<String> moduleNames = new HashSet<>(classes.length);
		for (Class<? extends IPlayerData> clazz : classes) {
			moduleNames.add(findModuleName(clazz));
		}
		for (String moduleName : this.modules.keySet()) {
			if (!moduleNames.contains(moduleName)) {
				this.modules.remove(moduleName);
				this.moduleNames.remove(moduleName);
			}
		}
	}

	@Override
	public T getIdentifier() {
		return this.identifier;
	}

	@Override
	public void setSession(ISession session) {
		this.session = session;
	}

	@Override
	public ISession getSession() {
		return session;
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
	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}

	@Override
	public long getActiveTime() {
		return activeTime;
	}
}