package com.keimons.platform.network;

/**
 * 数据传输结构
 * <p>
 * 底层数据传输中总要有个结构，最终定义为Json的底层数据载体
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class Packet {

	/**
	 * 消息号
	 */
	private int msgCode;

	/**
	 * 消息体
	 */
	private byte[] data;

	/**
	 * 错误号
	 */
	private String[] errCodes;

	public int getMsgCode() {
		return msgCode;
	}

	public Packet setMsgCode(int msgCode) {
		this.msgCode = msgCode;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public Packet setData(byte[] data) {
		this.data = data;
		return this;
	}

	public String[] getErrCodes() {
		return errCodes;
	}

	public Packet setErrCodes(String[] errCodes) {
		this.errCodes = errCodes;
		return this;
	}
}