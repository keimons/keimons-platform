package com.keimons.nutshell.network.coder;

import com.keimons.nutshell.unit.ClassUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 消息适配器
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public abstract class CodecAdapter<OUT_BOUND> extends MessageToMessageCodec<byte[], OUT_BOUND> {

	@SuppressWarnings("unchecked")
	private Class<OUT_BOUND> messageType = (Class<OUT_BOUND>) ClassUtil.findGenericType(
			this, CodecAdapter.class, "OUT_BOUND"
	);

	public Class<OUT_BOUND> getMessageType() {
		return messageType;
	}


	@Override
	protected void encode(ChannelHandlerContext ctx, OUT_BOUND msg, List<Object> out) throws Exception {
		out.add(encode(msg));
	}

	public abstract byte[] encode(OUT_BOUND msg);

	@Override
	protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
		out.add(decode(msg));
	}

	public abstract OUT_BOUND decode(byte[] msg);

	public abstract int getMsgCode(Object msg);
}