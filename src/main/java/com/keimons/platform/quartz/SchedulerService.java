package com.keimons.platform.quartz;

import com.keimons.platform.annotation.AJob;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 调度模块
 * <p>
 * 用于处理程序中的定时任务
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public class SchedulerService {

	/**
	 * 创建Scheduler的工厂
	 */
	private static Scheduler scheduler = null;

	/**
	 * 已经加载的任务
	 */
	private static Set<Class<?>> loaded = new HashSet<>();

	/**
	 * 增加一个任务
	 *
	 * @param jobDetail 任务描述
	 * @param trigger   触发器
	 */
	public static void addJob(JobDetail jobDetail, Trigger trigger) {
		TriggerKey triggerKey = trigger.getKey();
		JobKey jobKey = JobKey.jobKey(triggerKey.getName(), triggerKey.getGroup());
		try {
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 删除任务
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			LogService.error(e, "调度任务添加失败：旧任务移除失败！");
		}

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			LogService.error(e);
		}
	}

	/**
	 * 加载所有定时任务
	 *
	 * @param packageName 包名
	 */
	@SuppressWarnings("unchecked")
	public static void addJobs(String packageName) {
		List<Class<Object>> classes = ClassUtil.findClasses(packageName, AJob.class);
		for (Class<?> clazz : classes) {
			if (loaded.contains(clazz)) {
				continue;
			}
			AJob info = clazz.getAnnotation(AJob.class);
			if (!Job.class.isAssignableFrom(clazz)) {
				throw new ModuleException("Group: " + info.JobGroup() + ", Name: " + info.JobName() + ", 调度任务安装失败！未实现Job接口！");
			}
			Class<Job> clazz1 = (Class<Job>) clazz;
			System.out.println("正在安装定时任务：" + clazz.getSimpleName());

			// 任务触发器
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(info.JobName(), info.JobGroup())
					.withSchedule(CronScheduleBuilder.cronSchedule(info.JobCron()))
					.forJob(JobKey.jobKey(info.JobName(), info.JobGroup()))
					.build();
			// 任务描述
			JobDetail detail = JobBuilder.newJob(clazz1)
					.withIdentity(info.JobName(), info.JobGroup())
					.build();

			addJob(detail, trigger);
			loaded.add(clazz);
			System.out.println("成功安装定时任务：" + clazz.getSimpleName());
		}
	}

	/**
	 * 初始化定时任务系统
	 */
	public static void init() {
		// 从工厂中获取调度器实例
		// Quartz默认启动10个线程，考虑到各种保存数据，可能会长时间占用一个线程，例如保存玩家，保存排行榜等
		// 允许Quartz启动10个线程，暂时不考虑修改
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			LogService.error(e);
		}
	}

	/**
	 * 关闭定时任务系统
	 */
	public static void shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			LogService.error(e);
		}
	}
}