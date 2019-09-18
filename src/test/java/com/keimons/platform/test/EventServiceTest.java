package com.keimons.platform.test;

import com.keimons.platform.event.Event;
import com.keimons.platform.event.EventService;
import com.keimons.platform.iface.IEventCode;
import com.keimons.platform.iface.IEventHandler;

/**
 * 事件系统测试
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 */
public class EventServiceTest implements IEventHandler {

	public static void main(String[] args) {
		EventService.init();
		EventService.registerEvent(new EventServiceTest());
		EventService.publicEvent(null, TestEventCodeEnum.LOGIN, "测试事件系统");
		EventService.shutdown();
	}

	@Override
	public IEventCode[] register() {
		return new IEventCode[]{TestEventCodeEnum.LOGIN, TestEventCodeEnum.LOGOUT};
	}

	@Override
	public void handler(Event event) {
		TestEventCodeEnum eventCode = (TestEventCodeEnum) event.getEventCode();
		switch (eventCode) {
			case LOGIN: {
				String params = event.getParams(0);
				System.out.println(params);
			}
			break;
			default:
				System.out.println("未处理");
		}
	}
}