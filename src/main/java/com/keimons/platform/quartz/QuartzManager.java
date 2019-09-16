package com.keimons.platform.quartz;

import com.keimons.platform.annotation.AJob;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;
import java.util.function.Consumer;

public class QuartzManager {

	/**
	 * 创建Scheduler的工厂
	 */
	private static Scheduler scheduler = null;

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
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改定时任务的描述
	 *
	 * @param name     任务名称
	 * @param group    任务分组
	 * @param consumer 操作方式
	 */
	public static void modifyJobDetail(String name, String group, Consumer<JobDetail> consumer) {
		JobKey jobKey = JobKey.jobKey(name, group);
		TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
		try {
			Trigger trigger = scheduler.getTrigger(triggerKey);
			if (trigger == null) {
				return;
			}
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

			consumer.accept(detail);
			CronScheduleBuilder corn = (CronScheduleBuilder) trigger.getScheduleBuilder();
			corn.withMisfireHandlingInstructionDoNothing();
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(name, group)
					.withSchedule(corn)
					.startNow().build();

			addJob(detail, trigger);

			System.out.println("修改任务【" + name + "】");
		} catch (SchedulerException e) {
			System.err.println("修改任务失败【" + name + "】");
			e.printStackTrace();
		}
	}

	/**
	 * 加载所有定时任务
	 *
	 * @param packageName 包名
	 */
	private void loadJobs(String packageName) {
		List<Class<BaseJob>> classes = ClassUtil.load(packageName, AJob.class, BaseJob.class);
		for (Class<BaseJob> clazz : classes) {
			try {
				BaseJob job = clazz.newInstance();
				addJob(job);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void init(String packageName) {
		// 从工厂中获取调度器实例
		// Quartz默认启动10个线程，考虑到各种保存数据，可能会长时间占用一个线程，例如保存玩家，保存排行榜等
		// 允许Quartz启动10个线程，暂时不考虑修改
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			LogService.error(e);
		}

		loadJobs(packageName);

		try {
			// 启动
			scheduler.start();
		} catch (SchedulerException e) {
			LogService.error(e);
		}
	}

	public void reload() {
		System.out.println(this.getClass().toString() + "!!!");
	}

	public boolean shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return true;
	}
}