package com.keimons.nutshell.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.keimons.nutshell.process.IProcessor;
import com.keimons.nutshell.session.Session;


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
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProtobufHandler<MessageT extends Message> extends BaseHandler<Session, ByteString, MessageT> {

	private Parser<MessageT> parser;

	@SuppressWarnings("unchecked")
	public ProtobufHandler(IProcessor<Session, MessageT> processor,
						   int msgCode, int committerStrategy, int executorStrategy,
						   int interval, String desc) {
		super(processor, msgCode, committerStrategy, executorStrategy, interval, desc);

		Class<MessageT> messageType = getMessageType();
		MessageT instance = Internal.getDefaultInstance(messageType);
		parser = (Parser<MessageT>) instance.getParserForType();
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