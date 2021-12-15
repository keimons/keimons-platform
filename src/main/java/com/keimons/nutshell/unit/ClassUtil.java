package com.keimons.nutshell.unit;

import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;
import com.keimons.nutshell.module.AnnotationNotFoundException;
import jdk.internal.vm.annotation.ForceInline;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * 读取某一个jar包或者某一个文件夹下的所有class文件
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class ClassUtil {

	private static final ILogger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 判断一个类是否是一个普通类
	 *
	 * @param clazz 类
	 * @return 是否普通类
	 */
	private static boolean isNormalClass(Class<?> clazz) {
		int modifiers = Modifier.ABSTRACT | Modifier.INTERFACE;
		return (clazz.getModifiers() & modifiers) == 0;
	}

	/**
	 * 是否包含注解
	 *
	 * @param clazz  需要查找注解的类
	 * @param target 注解类
	 * @return 是否包含该注解
	 * @throws AnnotationNotFoundException 注解查找失败异常
	 */
	@ForceInline
	public static boolean hasAnnotation(
			Class<?> clazz, Class<? extends Annotation> target) {
		Annotation annotation = clazz.getAnnotation(target);
		return annotation != null;
	}

	/**
	 * 判断一个类是否包含某个注解
	 *
	 * @param clazz
	 * @param target
	 * @param <T>
	 * @return
	 */
	public static <T extends Annotation> T findAnnotation(
			Class<?> clazz, Class<T> target) {
		T annotation = clazz.getAnnotation(target);
		if (annotation == null) {
			throw new AnnotationNotFoundException(clazz, target);
		}
		return annotation;
	}

	/**
	 * 加载所有使用该注解的类
	 *
	 * @param packageName 包名
	 * @param target      注解
	 * @param <T>         泛型类型
	 * @return 使用该注解的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> findClassesWithInterface(
			String packageName, Class<?> target, Class<? extends Annotation> ext) {
		if (!Modifier.isInterface(target.getModifiers())) {
			String info = "target class " + target.getSimpleName() + " not interface";
			throw new IllegalStateException(info);
		}
		return ClassUtil.findClasses(packageName).stream()
				.filter(target::isAssignableFrom)
				.filter(ClassUtil::isNormalClass)
				.map(clazz -> (Class<T>) clazz)
				.collect(Collectors.toList());
	}

	/**
	 * 加载所有使用该注解的类
	 *
	 * @param packageName 包名
	 * @param annotation  注解
	 * @param <T>         泛型类型
	 * @return 使用该注解的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<T>> findClasses(
			String packageName, Class<? extends Annotation> annotation) {
		return ClassUtil.findClasses(packageName).stream()
				.filter(clazz -> clazz.isAnnotationPresent(annotation))
				.filter(ClassUtil::isNormalClass)
				.map(clazz -> (Class<T>) clazz)
				.collect(Collectors.toList());
	}

	/**
	 * 从包package中获取所有的Class
	 *
	 * @param pack 包名
	 * @return 该包下所有的class文件
	 */
	private static Set<Class<?>> findClasses(String pack) {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<>();
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(pack, filePath, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								// 如果是一个.class文件 而且不是目录
								if (name.endsWith(".class") && !entry.isDirectory()) {
									// 去掉后面的".class" 获取真正的类名
									String className = name.substring(packageName.length() + 1, name.length() - 6);
									try {
										// 添加到classes
										classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
									} catch (ClassNotFoundException e) {
										logger.error(e);
									}
								}
							}
						}
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}

		return classes;
	}

	/**
	 * 查找一个对象的泛型类型
	 *
	 * @param object   对象
	 * @param clazz    类型
	 * @param typeName 泛型参数名
	 * @return 类型
	 */
	public static <T> Class<T> findGenericType(final Object object, Class<?> clazz, String typeName) {
		if (clazz.isInterface()) {
			return findGenericTypeInInterface(object, clazz, typeName);
		} else {
			return findGenericTypeInClass(object, clazz, typeName);
		}
	}

	/**
	 * 在类中查找泛型类型
	 *
	 * @param object   要查找泛型类型的对象
	 * @param clazz    要查找泛型的类
	 * @param typeName 查找的泛型字段名
	 * @param <T>      类类型
	 * @return 类
	 */
	@SuppressWarnings("unchecked")
	private static <T> Class<T> findGenericTypeInClass(
			final Object object, Class<?> clazz, String typeName) {
		final Class<?> thisClass = object.getClass();
		Class<?> currentClass = thisClass;
		for (; ; ) {
			TypeVariable<? extends Class<?>>[] typeParameters = currentClass.getTypeParameters();
			if (currentClass.getSuperclass() == clazz) {
				int typeParamIndex = -1;
				TypeVariable<?>[] typeParams = currentClass.getSuperclass().getTypeParameters();
				for (int i = 0; i < typeParams.length; i++) {
					if (typeName.equals(typeParams[i].getName())) {
						typeParamIndex = i;
						break;
					}
				}

				if (typeParamIndex < 0) {
					throw new IllegalStateException(
							"unknown type parameter '" + typeName + "': " + clazz);
				}

				Type genericSuperType = currentClass.getGenericSuperclass();
				if (!(genericSuperType instanceof ParameterizedType)) {
					return (Class<T>) Object.class;
				}

				Type[] actualTypeParams = ((ParameterizedType) genericSuperType).getActualTypeArguments();

				Type actualTypeParam = actualTypeParams[typeParamIndex];
				if (actualTypeParam instanceof ParameterizedType) {
					actualTypeParam = ((ParameterizedType) actualTypeParam).getRawType();
				}
				if (actualTypeParam instanceof Class) {
					return (Class<T>) actualTypeParam;
				}
				if (actualTypeParam instanceof GenericArrayType) {
					Type componentType = ((GenericArrayType) actualTypeParam).getGenericComponentType();
					if (componentType instanceof ParameterizedType) {
						componentType = ((ParameterizedType) componentType).getRawType();
					}
					if (componentType instanceof Class) {
						return (Class<T>) Array.newInstance((Class<?>) componentType, 0).getClass();
					}
				}
				if (actualTypeParam instanceof TypeVariable) {
					// Resolved type parameter points to another type parameter.
					TypeVariable<?> v = (TypeVariable<?>) actualTypeParam;
					currentClass = thisClass;
					if (!(v.getGenericDeclaration() instanceof Class)) {
						return (Class<T>) Object.class;
					}

					clazz = (Class<?>) v.getGenericDeclaration();
					typeName = v.getName();
					if (clazz.isAssignableFrom(thisClass)) {
						continue;
					} else {
						return (Class<T>) Object.class;
					}
				}

				fail(thisClass, typeName);
			}
			currentClass = currentClass.getSuperclass();
			if (currentClass == null) {
				fail(thisClass, typeName);
			}
		}
	}

	/**
	 * 在接口中查找泛型类型
	 *
	 * @param object   要查找泛型类型的对象
	 * @param clazz    要查找泛型的类
	 * @param typeName 查找的泛型字段名
	 * @param <T>      类类型
	 * @return 类
	 */
	@SuppressWarnings("unchecked")
	private static <T> Class<T> findGenericTypeInInterface(
			final Object object, Class<?> clazz, String typeName) {
		Type[] interfaces = object.getClass().getGenericInterfaces();
		for (Type interfaceType : interfaces) {
			if (interfaceType instanceof Class) {
				continue;
			}
			ParameterizedType paramType = (ParameterizedType) interfaceType;
			if (paramType.getRawType() != clazz) {
				continue;
			}
			Class<?> rawType = (Class<?>) paramType.getRawType();
			TypeVariable<? extends Class<?>>[] variables = rawType.getTypeParameters();
			int index = -1;
			for (int i = 0; i < variables.length; i++) {
				if (variables[i].getName().equals(typeName)) {
					index = i;
					break;
				}
			}
			if (index < 0) {
				return null;
			}
			Type argument = paramType.getActualTypeArguments()[index];
			if (argument instanceof Class) {
				return (Class<T>) argument;
			} else if (argument instanceof TypeVariableImpl) {
				TypeVariableImpl<?> variable = (TypeVariableImpl<?>) argument;
				Type[] bounds = variable.getBounds();
				if (bounds.length == 1 && bounds[0] instanceof Class) {
					return (Class<T>) bounds[0];
				} else {
					// TODO 该泛型实际继承自多个接口
				}
			}
		}
		return null;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 *
	 * @param packageName 包名称
	 * @param packagePath 包路径
	 * @param classes     文件
	 */
	private static void findAndAddClassesInPackageByFile(
			String packageName,
			String packagePath,
			Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
		File[] files = dir.listFiles(file -> file.isDirectory() || (file.getName().endsWith(".class")));
		// 循环所有文件
		for (File file : Objects.requireNonNull(files)) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				if (packageName.equals("")) {
					findAndAddClassesInPackageByFile(file.getName(), file.getAbsolutePath(), classes);
				} else {
					findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), classes);
				}
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' + className));
					// 这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * 加载所有使用该注解的package-info文件
	 *
	 * @param packageName 包名
	 * @param annotation  注解
	 * @return 使用该注解的类
	 */
	public static List<Package> loadPackages(String packageName, Class<? extends Annotation> annotation) {
		return ClassUtil.getPackages(packageName)
				.stream()
				.filter(clazz -> clazz.isAnnotationPresent(annotation))
				.collect(Collectors.toList());
	}

	/**
	 * 从包package中获取所有的Package
	 *
	 * @param pack 包名
	 * @return 该包下所有的package-info文件
	 */
	public static Set<Package> getPackages(String pack) {
		// 第一个class类的集合
		Set<Package> packages = new LinkedHashSet<>();
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findPackageByFile(pack, filePath, packages);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								// 如果是一个.class文件 而且不是目录
								if (name.endsWith("package-info.class") && !entry.isDirectory()) {
									try {
										Thread.currentThread().getContextClassLoader().loadClass(packageName + ".package-info.class");
										packages.add(Package.getPackage(packageName));
									} catch (ClassNotFoundException e) {
										// 添加用户自定义视图类错误 找不到此类的.class文件
										logger.error(e);
									}
								}
							}
						}
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}

		return packages;
	}

	/**
	 * 以文件的形式来获取包下的所有package-info文件
	 *
	 * @param packageName 包名称
	 * @param packagePath 包路径
	 * @param packages    文件
	 */
	private static void findPackageByFile(String packageName, String packagePath, Set<Package> packages) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// error.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		// 自定义过滤规则 过滤目录 或者 package-info.class文件
		File[] files = dir.listFiles(file -> file.isDirectory() || (file.getName().equals("package-info.class")));
		// 循环所有文件
		for (File file : Objects.requireNonNull(files)) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				if (packageName.equals("")) {
					findPackageByFile(file.getName(), file.getAbsolutePath(), packages);
				} else {
					findPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), packages);
				}
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				if (className.endsWith("package-info")) {
					try {
						// 添加到集合中去
						// classes.add(Class.forName(packageName + '.' + className));
						// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
						Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
						packages.add(Package.getPackage(packageName));
					} catch (ClassNotFoundException e) {
						// error.error("添加用户自定义视图类错误 找不到此类的.class文件");
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 查找失败
	 *
	 * @param type  类文件
	 * @param param 参数名
	 * @throws IllegalStateException 抛出异常，并且不会返回任何内容
	 */
	private static void fail(Class<?> type, String param) {
		throw new IllegalStateException(
				"泛型类型参数查找失败 '" + param + "': " + type);
	}
}