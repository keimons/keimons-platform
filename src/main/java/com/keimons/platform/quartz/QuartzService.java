package com.keimons.platform.quartz;

import com.keimons.platform.annotation.AJob;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 定时任务系统
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 */
public class QuartzService {

	/**
	 * 创建Scheduler的工厂
	 */
	private static Scheduler scheduler = null;

	/**
	 * 已经加载的任务
	 */
	private static Set<Class<?>> loaded = new HashSet<>();

	/**
	 * 新增一个定时任务
	 *
	 * @param job 任务
	 */
	public static void addJob(BaseJob job) {
		addJob(job.getJobDetail(), job.getTrigger());
	}

	/**
	 * 增加一个任务
	 *
	 * @param jobDetail 任务描述
	 * @param trigger   触发器
	 */
	private static void addJob(JobDetail jobDetail, Trigger trigger) {
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			LogService.error(e);
		}
	}

	/**
	 * 获取定时任务的描述
	 *
	 * @param name  任务名称
	 * @param group 任务分组
	 * @return 任务描述
	 */
	public static JobDetail getJobDetail(String name, String group) {
		JobKey jobKey = JobKey.jobKey(name, group);
		try {
			return scheduler.getJobDetail(jobKey);
		} catch (SchedulerException e) {
			LogService.error(e);
		}
		return null;
	}

	/**
	 * 修改定时任务的描述
	 *
	 * @param job 任务
	 */
	public static void modifyJobDetail(BaseJob job) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				return;
			}
			JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
			JobDetail detail = scheduler.getJobDetail(jobKey);
			if (detail == null) {
				return;
			}
			// 停止触发器
			scheduler.pauseTrigger(triggerKey);
			// 移除触发器
			scheduler.unscheduleJob(triggerKey);
			// 删除任务
			scheduler.deleteJob(jobKey);
			addJob(job.getJobDetail(), job.getTrigger());
			System.out.println("修改任务【" + job.getName() + "】");
		} catch (SchedulerException e) {
			LogService.error(e, "修改任务失败【" + job.getGroup() + "】");
		}
	}

	/**
	 * 加载所有定时任务
	 *
	 * @param packageName 包名
	 */
	public static void addJobs(String packageName) {
		List<Class<Job>> classes = ClassUtil.load(packageName, AJob.class);
		for (Class<Job> clazz : classes) {
			if (loaded.contains(clazz)) {
				continue;
			}
			System.out.println("正在安装定时任务：" + clazz.getSimpleName());

			AJob info = clazz.getAnnotation(AJob.class);
			try {
				BaseJob job = new BaseJob("Keimons", info.JobName(), info.JobCron(), clazz.newInstance());
				addJob(job);
				loaded.add(clazz);
			} catch (InstantiationException | IllegalAccessException e) {
				LogService.error(e);
			}
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
	public static boolean shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			LogService.error(e);
		}
		return true;
	}
}