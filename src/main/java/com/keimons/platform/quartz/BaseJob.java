package com.keimons.platform.quartz;

import org.quartz.*;

/**
 * 定时任务描述，包括任务的触发器和描述
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.0
 */
public class BaseJob {

	/**
	 * 任务名称
	 */
	private final String name;

	/**
	 * 任务分组
	 */
	private final String group;

	/**
	 * 任务定时
	 */
	private final CronScheduleBuilder cron;

	/**
	 * 触发器
	 */
	private final Trigger trigger;

	/**
	 * 任务描述
	 */
	private final JobDetail jobDetail;

	/**
	 * 定时任务
	 *
	 * @param group 任务组
	 * @param name  任务名
	 * @param cron  任务触发表达式
	 * @param job   任务
	 */
	public BaseJob(String group, String name, String cron, Job job) {
		this.cron = CronScheduleBuilder.cronSchedule(cron);
		this.name = name;
		this.group = group;
		trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(this.cron).forJob(JobKey.jobKey(name, group)).build();
		jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(name, group).build();
	}

	/**
	 * 获取任务描述
	 *
	 * @return 任务描述
	 */
	public final JobDetail getJobDetail() {
		return jobDetail;
	}

	/**
	 * 获取任务触发器
	 *
	 * @return 触发器
	 */
	public final Trigger getTrigger() {
		return trigger;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}
}