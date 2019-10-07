package com.keimons.platform.unit;

import com.keimons.platform.log.LogService;
import io.netty.util.internal.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DM5工具类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class MD5Util {

	/**
	 * 计算字节数组的MD5
	 *
	 * @param bytes 字节数组
	 * @return MD5
	 */
	public static String md5(byte[] bytes) {
		// 获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			// update(byte[])方法，输入原数据
			// 类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
			md5.update(bytes);
			// digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
			// digest()返回值16位长度的哈希值，由byte[]承接
			byte[] md5Array = md5.digest();
			// byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
			return StringUtil.toHexString(md5Array);
		} catch (NoSuchAlgorithmException e) {
			LogService.error(e);
			return null;
		}
	}
}