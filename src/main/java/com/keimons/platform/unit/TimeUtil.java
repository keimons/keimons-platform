package com.keimons.platform.unit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 */
public class TimeUtil {

	/**
	 * 时间偏移量
	 */
	private static long offsetTime;

	/**
	 * 0时 时区时间偏移量<br />
	 * 东加西减 中国UTC+8<br />
	 * 应该在{@link System#currentTimeMillis()}的基础上加8个小时
	 */
	private static long offsetZero0 = Calendar.getInstance().get(Calendar.ZONE_OFFSET);

	/**
	 * 5时 时区时间偏移量<br />
	 * 东加西减 中国UTC+8<br />
	 * 应该在{@link System#currentTimeMillis()}的基础上加8个小时
	 */
	private static long offsetZero5 = Calendar.getInstance().get(Calendar.ZONE_OFFSET) - 5 * 60 * 60 * 1000L;

	/**
	 * 时间格式
	 */
	private final static DateTimeFormatter log = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * 时间格式
	 */
	private final static DateTimeFormatter order = DateTimeFormatter.ofPattern("yyyyMMdd000000000");

	/**
	 * 标准程序中标准日期格式
	 */
	private final static DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static long getOffsetTime() {
		return offsetTime;
	}

	public static void setOffsetTime(long offsetTime) {
		TimeUtil.offsetTime = offsetTime;
	}

	/**
	 * 当前系统时间
	 *
	 * @return 系统当前时间 毫秒值
	 */
	public static long currentTimeMillis() {
		return System.currentTimeMillis() + offsetTime;
	}

	/**
	 * 当前系统时间
	 *
	 * @return 当前的系统日期
	 */
	public static Date currentDate() {
		return new Date(currentTimeMillis());
	}

	public static String logDate() {
		return log.format(LocalDateTime.now());
	}

	/**
	 * 获取订单日期
	 *
	 * @return 订单日期
	 */
	public static long orderTime() {
		Instant instant = Instant.ofEpochMilli(System.currentTimeMillis() + offsetTime);
		return Long.parseLong(order.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault())));
	}

	/**
	 * 按照0点划分 判断两个日期是否同一天
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return 是否同一天
	 */
	public static boolean isSameDay0(long time1, long time2) {
		return isSameDay(time1, time2, offsetZero0);
	}

	/**
	 * 按照5点划分 判断两个日期是否同一天
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return 是否同一天
	 */
	public static boolean isSameDay5(long time1, long time2) {
		return isSameDay(time1, time2, offsetZero5);
	}

	/**
	 * 依赖服务器时区的是否同一天
	 *
	 * @param time1  时间1
	 * @param time2  时间2
	 * @param offset 时间偏移
	 * @return 是否同一天
	 */
	private static boolean isSameDay(long time1, long time2, long offset) {
		return (time1 + offset) / (24 * 60 * 60 * 1000L) == (time2 + offset) / (24 * 60 * 60 * 1000L);
	}

	public static int getYear() {
		return getTime(Calendar.YEAR);
	}

	public static int getMonth() {
		return getTime(Calendar.MONTH); //todo 周日从1开始
	}

	/**
	 * 当前周几
	 *
	 * @return 1-7 代表周一到周日
	 */
	public static int getDayOfWeek() {
		int dayOfWeek = getTime(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek == 0 ? 7 : dayOfWeek;
	}

	/**
	 * 当前周几
	 *
	 * @return 1-7 代表周一到周日
	 */
	public static int getHouseOfMonth() {
		return (getDayOfMonth() - 1) * 24 + getHourOfDay();
	}

	/**
	 * 这个月的第几天
	 *
	 * @return
	 */
	public static int getDayOfMonth() {
		return getTime(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 几点
	 *
	 * @return
	 */
	public static int getHourOfDay() {
		return getTime(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 当前是一天中的第x分钟
	 *
	 * @return 第x分钟
	 */
	public static int getMinuteOfDay() {
		return getTime(Calendar.HOUR_OF_DAY) * 60 + getTime(Calendar.MINUTE);
	}

	/**
	 * 第几分钟
	 *
	 * @return
	 */
	public static int getMinuteOfHouse() {
		return getTime(Calendar.MINUTE);
	}

	/**
	 * 今天的第多少秒
	 *
	 * @return 今天的第多少秒
	 */
	public static int getSecondOfDay() {
		Calendar timeNow = Calendar.getInstance();
		Calendar timeZero = Calendar.getInstance();
		timeZero.set(Calendar.HOUR_OF_DAY, 0);
		timeZero.set(Calendar.MINUTE, 0);
		timeZero.set(Calendar.SECOND, 0);
		return (int) (timeNow.getTimeInMillis() - timeZero.getTimeInMillis()) / 1000;
	}

	public static int getTime(int type) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(currentTimeMillis());
		return instance.get(type);
	}


	/**
	 * 判断是否是同一周
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isSameWeek(long startTime, long endTime) {
		Calendar startInstance = Calendar.getInstance();
		startInstance.setTimeInMillis(startTime);
		Calendar endInstance = Calendar.getInstance();
		endInstance.setTimeInMillis(endTime);
		if (endTime - startTime >= 7 * 24 * 60 * 60 * 1000) {
			return false;
		}
		return startInstance.get(Calendar.WEEK_OF_YEAR) == endInstance.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 是否是同一个月
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isSameMonth(long startTime, long endTime) {
		Calendar startInstance = Calendar.getInstance();
		startInstance.setTimeInMillis(startTime);
		Calendar endInstance = Calendar.getInstance();
		endInstance.setTimeInMillis(endTime);
		return startInstance.get(Calendar.YEAR) == endInstance.get(Calendar.YEAR) &&
				startInstance.get(Calendar.MONTH) == endInstance.get(Calendar.MONTH);
	}

	/**
	 * 时间转化工具
	 *
	 * @param time 毫秒时间
	 * @return 日期字符串
	 */
	public static String convertTime(long time) {
		Instant instant = Instant.ofEpochMilli(time);
		return date.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
	}

	/**
	 * 时间转化工具
	 *
	 * @param date 日期时间 格式：yyyy-MM-dd HH:mm:ss
	 * @return 时间毫秒
	 */
	public static long convertTime(String date) {
		LocalDateTime parse = LocalDateTime.parse(date, TimeUtil.date);
		return parse.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}


	/**
	 * 获取一天的开始时间
	 * <p>
	 * yyyy-MM-dd 00:00:00.000
	 *
	 * @return 今天的开始时间
	 */
	public static long beginTime() {
		Instant instant = Instant.ofEpochMilli(currentTimeMillis());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDateTime beginTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
		return beginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 获取一天的开始时间
	 * <p>
	 * yyyy-MM-dd 23:59:59.999
	 *
	 * @return 今天的结束时间
	 */
	public static long endTime() {
		Instant instant = Instant.ofEpochMilli(currentTimeMillis());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDateTime beginTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return beginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 分钟转毫秒
	 *
	 * @param minute 分钟
	 * @return 毫秒
	 */
	public static long minuteToMillis(int minute) {
		return minute * 60L * 1000;
	}

	/**
	 * 小时转毫秒
	 *
	 * @param house 小时
	 * @return 毫秒
	 */
	public static long houseToMillis(int house) {
		return house * 60L * 60 * 1000;
	}

	/**
	 * 服务器活动时间
	 *
	 * @param startDate
	 * @param openTime
	 * @return
	 */
	public static long createActivityTime(String startDate, long openTime) {
		String[] split = startDate.split("\\|");
		int day = Integer.parseInt(split[0]) - 1;
		int hour = Integer.parseInt(split[1]);
		return openTime + day * 24 * 60 * 60 * 1000 + hour * 60 * 60 * 1000;
	}

	/**
	 * 开服第几天开启
	 *
	 * @param day
	 * @param hour
	 * @param openTime
	 * @return
	 */
	public static long getTimeByServerStart(int day, int hour, long openTime) {
		return openTime + (day - 1) * 24 * 60 * 60 * 1000 + hour * 60 * 60 * 1000;
	}

	public static void main(String[] args) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(2019, 6, 28, 4, 59, 59);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2019, 6, 28, 5, 0, 0);
		System.out.println(isSameDay5(calendar1.getTimeInMillis(), calendar2.getTimeInMillis()));
		System.out.println(convertTime(1565584200000L));


	}
}