package com.keimons.platform.event;

public class EventFactory implements com.lmax.disruptor.EventFactory<Event> {

	@Override
	public Event newInstance() {
		return new Event();
	}
}