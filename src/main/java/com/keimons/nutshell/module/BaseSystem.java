package com.keimons.nutshell.module;

import com.keimons.nutshell.unit.ClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 基础数据实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public abstract class BaseSystem {

	/**
	 * 系统数据 Key:数据名称 Value:数据
	 */
	protected final ConcurrentHashMap<String, IModule<? extends ISystemData>> modules = new ConcurrentHashMap<>();

	/**
	 * 获取玩家的一个模块
	 * <p>
	 * 如果玩家已经被完全加载，如果缺少这个模块，则补充这个模块，如果玩家没有被完全加载，则不能
	 *
	 * @param clazz 模块名称
	 * @param <V>   玩家数据类型
	 * @return 数据模块
	 * @throws AnnotationNotFoundException 如果{@code clazz}中不包含{@link ASystemData}注解，则抛出注解查找失败异常。
	 * @throws ModuleCreateFailException   模块创建失败异常
	 */
	public <V extends ISingularSystemData> V get(Class<V> clazz) {
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
	 * @throws ModuleCreateFailException 模块创建失败异常
	 */
	@NotNull
	private <V extends ISingularSystemData> ISingularModule<V> createSingularModule(Class<V> clazz) {
		String moduleName = findModuleName(clazz);
		synchronized (this) {
			if (!modules.containsKey(moduleName)) {
				try {
					Constructor<V> constructor = clazz.getDeclaredConstructor();
					addSingularData(constructor.newInstance());
				} catch (Exception e) {
					throw new ModuleCreateFailException(clazz, e);
				}
			}
		}
		return Objects.requireNonNull(findModule(clazz));
	}

	/**
	 * 增加一个模块数据
	 *
	 * @param data 模块
	 * @throws NullPointerException 如果{@code data} 为空，则抛出空指针异常。
	 */
	public void add(IRepeatedSystemData<?> data) {
		Objects.requireNonNull(data);
		addRepeatedData(data);
	}

	/**
	 * 获取玩家的一个模块
	 *
	 * @param clazz  模块名称
	 * @param dataId 数据唯一IDs
	 * @param <V>    模块类型
	 * @return 数据模块 {@code null} 数据不存在
	 * @throws AnnotationNotFoundException 如果{@code clazz}中不包含{@link ASystemData}注解，则抛出注解查找失败异常。
	 */
	@Nullable
	public <K, V extends IRepeatedSystemData<K>> V get(Class<V> clazz, K dataId) {
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
	 * @return 数据模块
	 * @throws AnnotationNotFoundException 如果{@code clazz}中不包含{@link ASystemData}注解，则抛出注解查找失败异常。
	 */
	public <K, V extends IRepeatedSystemData<K>> V remove(Class<V> clazz, K dataId) {
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
	 * @throws AnnotationNotFoundException 如果{@code clazz}中不包含{@link ASystemData}注解，则抛出注解查找失败异常。
	 */
	public <V extends ISystemData> String findModuleName(@NotNull Class<V> clazz) {
		ASystemData annotation = ClassUtil.findAnnotation(clazz, ASystemData.class);
		return annotation.moduleName();
	}

	/**
	 * 查找一个模块
	 *
	 * @param clazz 数据类型
	 * @param <V>   数据类型
	 * @param <R>   模块类型
	 * @return 模块 {@code null} 模块不存在
	 * @throws AnnotationNotFoundException 如果{@code clazz}中不包含{@link ASystemData}注解，则抛出注解查找失败异常。
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public <V extends ISystemData, R extends IModule<V>> R findModule(@NotNull Class<V> clazz) {
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
	protected <V extends IModule<? extends ISystemData>> V computeIfAbsent(
			String moduleName, Function<String, V> function) {
		return (V) modules.computeIfAbsent(moduleName, function);
	}

	/**
	 * 增加一个可重复的模块数据
	 *
	 * @param data 数据
	 */
	protected abstract <K> void addRepeatedData(IRepeatedSystemData<K> data);

	/**
	 * 增加一个非重复的模块数据
	 *
	 * @param data 数据
	 */
	protected abstract void addSingularData(ISingularSystemData data);

	/**
	 * 查找是否有这个模块
	 *
	 * @param classes 类
	 * @return 是否包含这些模块
	 */
	@SafeVarargs
	public final boolean hasModules(Class<? extends ISystemData>... classes) {
		for (Class<? extends ISystemData> clazz : classes) {
			ASystemData annotation = clazz.getAnnotation(ASystemData.class);
			if (annotation == null) {
				return false;
			}
			if (!modules.containsKey(annotation.moduleName())) {
				return false;
			}
		}
		return true;
	}
}