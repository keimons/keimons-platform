package com.keimons.platform.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机工具类
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class RandomUtil {

	/**
	 * 随机种子
	 */
	private static final Random random = new Random(TimeUtil.currentTimeMillis());

	/**
	 * 获取一个整数[start, end)
	 *
	 * @param min 最小值
	 * @param max 最大值
	 * @return 随机数
	 */
	public static int nextInt(int min, int max) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			int tmp = min;
			min = max;
			max = tmp;
		}
		return random.nextInt(max - min) + min;
	}

	/**
	 * 获取一个整数 [0, end)
	 *
	 * @param end 最大数
	 * @return 随机数
	 */
	public static int nextInt(int end) {
		return random.nextInt(end);
	}

	/**
	 * 随机一组不重复的数字
	 *
	 * @param list  数组
	 * @param limit 数量
	 * @param <T>   泛型
	 * @return 结果
	 */
	public static <T> List<T> random(List<T> list, int limit) {
		if (list.size() < limit) {
			return new ArrayList<>(list);
		}
		List<T> result = new ArrayList<>(limit);
		for (int i = 0; i < limit + 100; i++) {
			T t = list.get(RandomUtil.nextInt(list.size()));
			if (!result.contains(t)) {
				result.add(t);
				if (result.size() >= limit) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 随机一组不重复的数字
	 *
	 * @param list 数组
	 * @param <T>  泛型
	 * @return 结果
	 */
	public static <T> T random(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(RandomUtil.nextInt(list.size()));
	}

	/**
	 * 随机一组不重复的数字
	 *
	 * @param array 数组
	 * @param <T>   泛型
	 * @return 结果
	 */
	public static <T> T random(T[] array) {
		if (array.length == 0) {
			return null;
		}
		return array[RandomUtil.nextInt(array.length)];
	}
}