package com.keimons.nutshell.thread;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Nutshell类加载器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class NutshellClassLoader extends URLClassLoader {

	/**
	 * 类加载器名字
	 */
	private String name;

	/**
	 * 构造方法
	 *
	 * @param name 类装载器的名字
	 */
	public NutshellClassLoader(String name) {
		super(new URL[]{}, null
		);
		this.name = name;
	}

	/**
	 * 构造方法
	 *
	 * @param parent 父类加载
	 * @param name   类装载器的名字
	 */
	public NutshellClassLoader(ClassLoader parent, String name) {
		super(new URL[]{NutshellClassLoader.class.getResource("")}, parent);
		this.name = name;
	}

	/**
	 * 构造方法
	 *
	 * @param parent 父类加载
	 * @param name   类装载器的名字
	 */
	public NutshellClassLoader(URL[] urls, ClassLoader parent, String name) {
		super(urls, parent);
		this.name = name;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
//		try {
//			String fileName = "/" + name.replaceAll("\\.", "/") + ".class";
//			InputStream is = getClass().getResourceAsStream(fileName);
//			byte[] b = is.readAllBytes();
//			return defineClass(name, b, 0, b.length);
//		} catch (IOException e) {
//			throw new ClassNotFoundException(name);
//		}
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
		ClassLoader parent = NutshellClassLoader.class.getClassLoader();
		if (parent == null) {
			parent = ClassLoader.getSystemClassLoader();
		}
		return parent;
	}
}