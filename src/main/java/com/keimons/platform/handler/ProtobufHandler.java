package com.keimons.platform.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.keimons.platform.process.BaseHandler;
import com.keimons.platform.process.IProcessor;
import com.keimons.platform.session.Session;


/**
 * 消息号实现
 * <p>
 * Protobuf没有自描述性，所以，已知二级制内容，可以直接解析成任何一个同结构的我们想要的对象。
 * <b>规约：</b>
 * <blockquote><pre>{@code
 * syntax = "proto3";
 *
 * int32 msgCode = 1;
 * string errCode = 2;
 * bytes data = 3;
 *
 * }</pre></blockquote>
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufHandler<MessageT extends Message> extends BaseHandler<Session, ByteString, MessageT> {

	private Parser<MessageT> parser;

	@SuppressWarnings("unchecked")
	public ProtobufHandler(int msgCode, int interval, String desc,
						   IProcessor<Session, MessageT> processor) {
		super(msgCode, interval, desc, processor);

		Class<MessageT> messageType = getMessageType();
		MessageT instance = Internal.getDefaultInstance(messageType);
		parser = (Parser<MessageT>) instance.getParserForType();
	}

	public Runnable createTask(Session session, MessageT message) {
		return () -> {
			try {
				processor.processor(session, message);
			} finally {
				session.finish();
			}
		};
	}

	@Override
	public MessageT parseMessage(ByteString data) throws Exception {
		if (parser == null) {
			return null;
		}
		return parser.parseFrom(data);
	}

	public void setParser(Parser<MessageT> parser) {
		this.parser = parser;
	}

	public Parser<MessageT> getParser() {
		return parser;
	}
}