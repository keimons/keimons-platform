package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.keimons.platform.process.AProcessor;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;

/**
 * 默认实现
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
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