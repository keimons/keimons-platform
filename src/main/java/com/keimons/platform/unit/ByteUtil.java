package com.keimons.platform.unit;

import java.nio.ByteBuffer;

public class ByteUtil {

	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	/**
	 * byte 数组与 long 的相互转换
	 */
	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	/**
	 * byte 数组与 long 的相互转换
	 */
	public static long bytesToLong(byte[] bytes) {
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getLong();
	}

	/**
	 * byte 数组与 int 的相互转换
	 */
	public static byte[] longToBytes(int x) {
		buffer.putInt(0, x);
		return buffer.array();
	}

	/**
	 * byte 数组与 int 的相互转换
	 */
	public static int bytesToInt(byte[] bytes) {
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getInt();
	}
}