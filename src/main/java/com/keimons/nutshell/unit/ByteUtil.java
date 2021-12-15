package com.keimons.nutshell.unit;

import java.nio.ByteBuffer;

/**
 * 字节相关转化工具
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class ByteUtil {

	/**
	 * 长整型转字节数组
	 *
	 * @param value 长整数
	 * @return 字节数组
	 */
	public static byte[] longToBytes(long value) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, value);
		buffer.clear();
		return buffer.array();
	}

	/**
	 * 字节数组转长整型
	 *
	 * @param bytes 字节数组
	 * @return 长整型
	 */
	public static long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getLong();
	}

	/**
	 * 整型转字节数组
	 *
	 * @param value 整型
	 * @return 字节数组
	 */
	public static byte[] intToBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(0, value);
		return buffer.array();
	}

	/**
	 * 字节数组转整型
	 *
	 * @param bytes 字节数组
	 * @return 整型
	 */
	public static int bytesToInt(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getInt();
	}
}