package com.keimons.platform.network;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输结构
 */
@Getter
@Setter
public class Packet {

	private int msgCode;

	private byte[] data;

	private String[] errCodes;
}