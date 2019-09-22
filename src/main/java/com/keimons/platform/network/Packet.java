package com.keimons.platform.network;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据传输结构
 * <p>
 * 底层数据传输中总要有个结构，最终定义为Json的底层数据载体
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-22
 * @since 1.8
 */
@Getter
@Setter
public class Packet {

	private int msgCode;

	private byte[] data;

	private String[] errCodes;
}