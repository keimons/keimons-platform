package com.keimons.platform.test;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.quartz.QuartzService;

/**
 * 定时任务系统测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 **/
public class QuartzServiceTest {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty(KeimonsServer.PACKET, "com.keimons.platform.test");
		QuartzService.init();
//		Thread.sleep(14000L);
//		QuartzService.shutdown();
	}
}