package com.keimons.platform.session;

import com.keimons.platform.quartz.BaseJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 会话管理任务定时器 每5秒执行一次
 * <br />
 * 定时检测会话是否已经关闭或者会话空闲时间过长
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-20
 * @since 1.8
 */
public class SessionJob extends BaseJob {

	public SessionJob() {
		super("Keimons", "SessionLoop", "0/5 * * * * ? ");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SessionManager.getInstance().update();
	}
}
