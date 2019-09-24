package com.keimons.platform.unit;

import java.nio.ByteBuffer;

/**
 * 字节相关转化工具
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-25
 * @since 1.8
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