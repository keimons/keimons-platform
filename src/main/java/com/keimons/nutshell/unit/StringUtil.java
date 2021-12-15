package com.keimons.nutshell.unit;

import com.keimons.nutshell.log.ILogger;
import com.keimons.nutshell.log.LoggerFactory;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;

/**
 * 字符串工具类
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class StringUtil {

	private static final String REPLACE = "";

	private static final boolean CACHE_ENABLE;

	private static final ILogger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 标准字符串占位符
	 */
	public static final String TAG = "\u0001";

	public static final String CONST = "{}";

	/**
	 * Tag used to demarcate an ordinary argument.
	 */
	private static final char TAG_ARG = '\u0001';

	/**
	 * Tag used to demarcate a constant.
	 */
	private static final char TAG_CONST = '\u0002';

	static {
		CACHE_ENABLE = true;
	}

	/**
	 * 使用{@code \u0001}作为占位符的字符串填充
	 *
	 * @param format 字符串占位符
	 * @param params 填充参数
	 * @return 填充后的字符串
	 */
	public static String format(String format, Object... params) {
		try {
			ConstantCallSite site = (ConstantCallSite) StringConcatFactory.makeConcatWithConstants(MethodHandles.lookup(),
					"makeConcatWithConstants",
					MethodType.genericMethodType(params.length),
					format);
			return (String) site.dynamicInvoker().invokeWithArguments(params);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

//	public static void main(String[] args) {
//		String format = "这是一段测试代码，他是为了测试：" +
//				"byte(\u0001),boolean(\u0001)" +
//				"short(\u0001),int(\u0001)," +
//				"float(\u0001),lang(\u0001)," +
//				"double(\u0001),String(\u0001)," +
//				"Object(\u0001),Class(\u0001)";
//		System.out.println(format(format,
//				(byte) 1, true,
//				(short) -5, 1000,
//				-2.5f, 10000L,
//				13245.000D, "string",
//				new StringUtil(), StringUtil.class
//		));
//	}

	/**
	 * 字符串{@code 测试{}字符串}的加载方式
	 * <p>
	 * 首先按照{@code {}}对字符串进行分割。
	 *
	 * @param format
	 * @param params
	 * @return
	 */
	public static String format0(String format, Object... params) {
		String[] split = format.split("\\{\\}");

		StringBuilder sb = new StringBuilder();
		String str = format;
		for (int i = 0; i < params.length; i++) {
			int i1 = format.indexOf("{}");
			if (i1 == -1) {
				sb.append(str);
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println("1234".getBytes());
		System.out.println("我们1234".length());
	}

	/**
	 * 计算一个对象的字符串长度
	 *
	 * @param object 字符串
	 * @return 字符串长度
	 */
	public static int length(Object object) {
		Class<?> clazz = object.getClass();
		if (!clazz.isArray()) {
			if (clazz == Boolean.class) {
				return (boolean) object ? 4 : 5;
			} else if (clazz == Byte.class) {
				return stringSize((int) object);
			} else if (clazz == Character.class) {
				return 1;
			} else if (clazz == Short.class) {
				return stringSize((int) object);
			} else if (clazz == Integer.class) {
				return stringSize((int) object);
			} else if (clazz == Long.class) {
				return stringSize((long) object);
			} else {
				return String.valueOf(object).length();
			}
		} else {
			// check for primitive array types because they
			// unfortunately cannot be cast to Object[]
			if (object instanceof boolean[]) {
				return booleanArrayLength((boolean[]) object);
			} else if (object instanceof byte[]) {
				return byteArrayLength((byte[]) object);
			} else if (object instanceof char[]) {
				return charArrayLength((char[]) object);
			} else if (object instanceof short[]) {
				return shortArrayLength((short[]) object);
			} else if (object instanceof int[]) {
				return intArrayLength((int[]) object);
			} else if (object instanceof long[]) {
				return longArrayLength((long[]) object);
			} else {
				return objectArrayLength((Object[]) object);
			}
		}
	}

	private static int objectArrayLength(Object[] object) {
		int length = 0;
		for (Object b : object) {
			length += length(b);
		}
		return 2 + length + Math.max(object.length - 1, 0) << 1;
	}

	private static int longArrayLength(long[] object) {
		int length = 0;
		for (long b : object) {
			length += stringSize(b);
		}
		return 2 + length + Math.max(object.length - 1, 0) << 1;
	}

	private static int intArrayLength(int[] object) {
		int length = 0;
		for (int b : object) {
			length += stringSize(b);
		}
		return 2 + length + Math.max(object.length - 1, 0) << 1;
	}

	private static int shortArrayLength(short[] object) {
		int length = 0;
		for (short b : object) {
			length += stringSize(b);
		}
		return 2 + length + Math.max(object.length - 1, 0) << 1;
	}


	private static int charArrayLength(char[] object) {
		return 2 + object.length + Math.max(object.length - 1, 0) << 1;
	}

	private static int byteArrayLength(byte[] object) {
		int length = 0;
		for (byte b : object) {
			length += stringSize(b);
		}
		return 2 + length + Math.max(object.length - 1, 0) << 1;
	}

	private static int booleanArrayLength(boolean[] object) {
		int trueNum = 0;
		int falseNum = 0;
		for (boolean b : object) {
			if (b) trueNum++;
			else falseNum++;
		}
		return 2 + trueNum << 2 + falseNum * 5 + Math.max(object.length - 1, 0) << 1;
	}

	/**
	 * Returns the string representation size for a given int value.
	 *
	 * @param x int value
	 * @return string size
	 * @implNote There are other ways to compute this: e.g. binary search,
	 * but values are biased heavily towards zero, and therefore linear search
	 * wins. The iteration results are also routinely inlined in the generated
	 * code after loop unrolling.
	 */
	static int stringSize(int x) {
		int d = 1;
		if (x >= 0) {
			d = 0;
			x = -x;
		}
		int p = -10;
		for (int i = 1; i < 10; i++) {
			if (x > p)
				return i + d;
			p = 10 * p;
		}
		return 10 + d;
	}

	static int stringSize(long x) {
		int d = 1;
		if (x >= 0) {
			d = 0;
			x = -x;
		}
		long p = -10;
		for (int i = 1; i < 19; i++) {
			if (x > p)
				return i + d;
			p = 10 * p;
		}
		return 19 + d;
	}
}