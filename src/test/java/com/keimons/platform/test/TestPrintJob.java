package com.keimons.platform.test;

import com.keimons.platform.annotation.AJob;
import com.keimons.platform.log.LogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时输出测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 **/
@AJob(JobName = "TestPrint", JobCron = "0/5 * * * * ?")
public class TestPrintJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LogService.info("login successful!");
	}
}
