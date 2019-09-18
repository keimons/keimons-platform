package com.keimons.platform.event;

/**
 * 事件对象工厂
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-19
 * @since 1.0
 */
public class EventFactory implements com.lmax.disruptor.EventFactory<Event> {

	@Override
	public Event newInstance() {
		return new Event();
	}
}