package com.keimons.platform.module;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.keimons.platform.player.IPlayer;

/**
 * 如果这个道具需要展示给客户端看的话，需要实现这个接口
 */
public interface IBuild {

	/**
	 * 构造物品的客户端数据
	 *
	 * @return 物品数据
	 * @throws InvalidProtocolBufferException 构造数据失败
	 */
	default MessageLite build() throws InvalidProtocolBufferException {
		return null;
	}

	/**
	 * 构造物品的客户端数据
	 * <p>
	 * 公共玩家数据，这一部分数据由两部分构成
	 * 1.玩家私有数据
	 * 2.玩家和其他玩家共有数据
	 * <p>
	 * eg:
	 * 私有数据：玩家联盟数据
	 * 共有数据：联盟数据
	 *
	 * @param bytes 公共部分数据
	 * @return 物品数据
	 * @throws InvalidProtocolBufferException 构造数据失败
	 */
	default MessageLite build(ByteString bytes) throws InvalidProtocolBufferException {
		return build();
	}

	/**
	 * 构造物品的客户端数据
	 * <p>
	 * 公共玩家数据，这一部分数据由两部分构成
	 * 1.玩家私有数据
	 * 2.玩家和其他玩家共有数据
	 * <p>
	 * eg:
	 * 私有数据：玩家联盟数据
	 * 共有数据：联盟数据
	 *
	 * @param player 玩家
	 * @param bytes  公共部分数据
	 * @return 物品数据
	 * @throws InvalidProtocolBufferException 构造数据失败
	 */
	default MessageLite build(IPlayer player, ByteString bytes) throws InvalidProtocolBufferException {
		return build(bytes);
	}
}