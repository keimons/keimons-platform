package com.keimons.platform.quartz;

import com.keimons.platform.annotation.AJob;
import lombok.Getter;
import org.quartz.*;

/**
 * 基础任务描述，包括任务的触发器和描述
 */
@Getter
@AJob
public abstract class BaseJob implements Job {

	/**
	 * 任务名称
	 */
	private final String name;

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

	public BaseJob(String group, String name, String cron) {
		this.cron = CronScheduleBuilder.cronSchedule(cron);
		this.name = name;
		trigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(this.cron).build();
		jobDetail = JobBuilder.newJob(this.getClass()).withIdentity(name, group).build();
	}

	public final JobDetail getJobDetail() {
		return jobDetail;
	}

	public final Trigger getTrigger() {
		return trigger;
	}
}