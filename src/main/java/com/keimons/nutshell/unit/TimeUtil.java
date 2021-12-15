package com.keimons.nutshell.unit;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;
import java.util.Date;

/**
 * 线程安全的时间工具
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class TimeUtil {

	/**
	 * 时间偏移量
	 */
	private static long offsetTime;

	/**
	 * 标准程序中标准日期格式
	 */
	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * 短日期格式
	 */
	private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取系统时间偏移量
	 *
	 * @return 系统时间偏移量
	 */
	public static long getOffsetTime() {
		return offsetTime;
	}

	/**
	 * 设置系统时间偏移量
	 *
	 * @param offsetTime 系统时间偏移量
	 */
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

	/**
	 * 获取当前日期
	 * <p>
	 * 采用的是线程安全的{@link DateTimeFormatter}实现
	 *
	 * @return 当前日期 日期格式："yyyy-MM-dd HH:mm:ss.SSS"
	 */
	public static String getDateTime() {
		return formatter.format(LocalDateTime.ofInstant(
				Instant.ofEpochMilli(currentTimeMillis()), ZoneId.systemDefault()
		));
	}

	/**
	 * 获取当前日期
	 * <p>
	 * 采用的是线程安全的{@link DateTimeFormatter}实现
	 *
	 * @param time 时间刻度
	 * @return 当前日期 日期格式："yyyy-MM-dd HH:mm:ss.SSS"
	 */
	public static String getDateTime(long time) {
		return formatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
	}

	/**
	 * 按照0点划分 判断两个日期是否同一天
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return 是否同一天
	 */
	public static boolean isSameDay0(long time1, long time2) {
		return isSameDay(time1, time2, 0);
	}

	/**
	 * 按照5点划分 判断两个日期是否同一天
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return 是否同一天
	 */
	public static boolean isSameDay5(long time1, long time2) {
		return isSameDay(time1, time2, 5);
	}

	/**
	 * 按照某一个整点划分 判断两个日期是否同一天
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @param hour  划分时间（小时）
	 * @return 是否同一天
	 */
	public static boolean isSameDay(long time1, long time2, int hour) {
		LocalDateTime dateTime1 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time1), ZoneId.systemDefault());
		LocalDateTime dateTime2 = LocalDateTime.ofInstant(Instant.ofEpochMilli(time2), ZoneId.systemDefault());
		LocalDate date1;
		LocalDate date2;
		if (dateTime1.toLocalTime().isBefore(LocalTime.of(hour, 0))) {
			date1 = dateTime1.toLocalDate().plusDays(-1);
		} else {
			date1 = dateTime1.toLocalDate();
		}
		if (dateTime2.toLocalTime().isBefore(LocalTime.of(hour, 0))) {
			date2 = dateTime2.toLocalDate().plusDays(-1);
		} else {
			date2 = dateTime2.toLocalDate();
		}
		return date1.equals(date2);
	}

	/**
	 * 时间转化工具
	 *
	 * @param time 毫秒时间
	 * @return 日期字符串
	 */
	public static String convertTime(long time) {
		Instant instant = Instant.ofEpochMilli(time);
		return formatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
	}

	/**
	 * 时间转化工具
	 *
	 * @param date 日期时间 格式：yyyy-MM-dd HH:mm:ss
	 * @return 时间毫秒
	 */
	public static long convertTime(String date) {
		LocalDateTime parse = LocalDateTime.parse(date, dtf);
		return parse.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 获取一天的开始时间
	 * <p>
	 * yyyy-MM-dd 00:00:00.000
	 *
	 * @param millis 时间刻度
	 * @return 当天开始时间
	 */
	public static long beginTime(long millis) {
		LocalDate date = Instant.ofEpochMilli(millis).query(TemporalQueries.localDate());
		LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
		return beginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * 获取一天的结束时间
	 * <p>
	 * yyyy-MM-dd 23:59:59.999
	 *
	 * @param millis 时间刻度
	 * @return 当天结束时间
	 */
	public static long endTime(long millis) {
		LocalDate date = Instant.ofEpochMilli(millis).query(TemporalQueries.localDate());
		LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MAX);
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
}