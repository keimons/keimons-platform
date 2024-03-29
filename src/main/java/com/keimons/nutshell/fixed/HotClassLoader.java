package com.keimons.nutshell.fixed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 动态类加载
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class HotClassLoader extends ClassLoader {

	public HotClassLoader() {
		super(ClassLoader.getSystemClassLoader());
	}

	private File objFile;

	public File getObjFile() {
		return objFile;
	}

	public void setObjFile(File objFile) {
		this.objFile = objFile;
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class clazz = null;
		try {
			byte[] data = getClassFileBytes(getObjFile());
			clazz = defineClass(name, data, 0, data.length);//这个方法非常重要
			if (null == clazz) { // 如果在这个类加载器中都不能找到这个类的话，就真的找不到了

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;

	}

	/**
	 * 把CLASS文件转成BYTE
	 *
	 * @throws Exception 读取异常
	 */
	private byte[] getClassFileBytes(File file) throws Exception {
		//采用NIO读取
		FileInputStream fis = new FileInputStream(file);
		FileChannel fileC = fis.getChannel();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		WritableByteChannel outC = Channels.newChannel(baos);
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		for (; ; ) {
			int i = fileC.read(buffer);
			if (i == 0 || i == -1) {
				break;
			}
			buffer.flip();
			outC.write(buffer);
			buffer.clear();
		}
		fis.close();
		return baos.toByteArray();
	}
}