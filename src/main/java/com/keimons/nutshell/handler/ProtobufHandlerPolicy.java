package com.keimons.nutshell.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.keimons.nutshell.network.PbPacket;
import com.keimons.nutshell.process.AProcessor;
import com.keimons.nutshell.process.IProcessor;
import com.keimons.nutshell.session.Session;

/**
 * 默认实现
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProtobufHandlerPolicy extends BaseHandlerPolicy<Session, PbPacket.Packet, ByteString> {

	public ProtobufHandlerPolicy(int strategyIndex) {
		super(strategyIndex);
	}

	/**
	 * 加载所有消息号
	 *
	 * @param pkg 包体
	 */
	public void addHandler(String pkg) {
		super.<AProcessor, Message>addHandler(pkg, AProcessor.class, (clazz, annotation) -> {
			try {
				IProcessor<Session, Message> processor = clazz.getDeclaredConstructor().newInstance();
				return new ProtobufHandler<>(
						processor,
						annotation.MsgCode(),
						annotation.CommitterStrategy(),
						annotation.ExecutorStrategy(),
						annotation.Interval(),
						annotation.Desc()
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}

	@Override
	public void exceptionCaught(Session session, PbPacket.Packet packet, Throwable cause) {
		cause.printStackTrace();
	}
}