package com.keimons.platform.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author monkey1993
 * @version 1.0
 * @date 2020-06-16
 * @since 1.8
 **/
public class KeimonsClassLoader extends URLClassLoader {

	/**
	 * 类加载器名字
	 */
	private String name;

	/**
	 * 构造方法
	 *
	 * @param name 类装载器的名字
	 */
	public KeimonsClassLoader(String name) {
		super(new URL[]{}, null);
		this.name = name;
	}

	/**
	 * 构造方法
	 *
	 * @param parent 父类加载
	 * @param name   类装载器的名字
	 */
	public KeimonsClassLoader(ClassLoader parent, String name) {
		super(new URL[]{}, parent);
		this.name = name;
	}

	/**
	 * 构造方法
	 *
	 * @param parent 父类加载
	 * @param name   类装载器的名字
	 */
	public KeimonsClassLoader(URL[] urls, ClassLoader parent, String name) {
		super(urls, parent);
		this.name = name;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			String fileName = "/" + name.replaceAll("\\.", "/") + ".class";
			InputStream is = getClass().getResourceAsStream(fileName);
			byte[] b = is.readAllBytes();
			return defineClass(name, b, 0, b.length);
		} catch (IOException e) {
			throw new ClassNotFoundException(name);
		}
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * 定位基于当前上下文的父类加载器
	 *
	 * @return 返回可用的父类加载器.
	 */
	private static ClassLoader findParentClassLoader() {
		ClassLoader parent = KeimonsClassLoader.class.getClassLoader();
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}
		return parent;
	}
}