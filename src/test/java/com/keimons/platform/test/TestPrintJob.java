package com.keimons.platform.test;

import com.keimons.platform.quartz.BaseJob;
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
public class TestPrintJob extends BaseJob {

	/**
	 * 定时输出测试
	 */
	public TestPrintJob() {
		super("KeimonsTestGroup", "KeimonsTimePrint", "0/5 * * * * ? ");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Job executed!");
	}
}
