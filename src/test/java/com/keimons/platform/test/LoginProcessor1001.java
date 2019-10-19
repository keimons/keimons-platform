package com.keimons.platform.test;

import com.google.protobuf.Message;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.network.Packet;
import com.keimons.platform.player.IPlayer;
import com.keimons.platform.process.BaseProcessor;
import com.keimons.platform.process.ThreadLevel;
import com.keimons.platform.session.Session;

@AProcessor(MsgCode = 1001, Interval = 200, Desc = "登录协议", ThreadLevel = ThreadLevel.AUTO)
public class LoginProcessor1001 extends BaseProcessor {

	@Override
	public Packet process(Session session) {
		return null;
	}

	@Override
	public Packet process(Session session, Message request) {
		return null;
	}

	@Override
	public Packet process(IPlayer player) {
		return null;
	}

	@Override
	public Packet process(IPlayer player, Message request) {
		return null;
	}
}