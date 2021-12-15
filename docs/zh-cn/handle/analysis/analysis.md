## 消息解析 <!-- {docsify-ignore-all} -->

由于网络层存在粘包/半包情况，所以，服务器在收到消息后，需要对数据流进行二次封装，构造出来一个完整的包体。同时，当服务器发送数据时，也需要将一个包体编码成字节流，再发送到客户端。

* 接收数据时，消息解析策略负责将`byte[]`解析成系统中可识别数据，例如`json`、`protobuf`等数据结构。

* 发送数据时，消息解析策略负责将`json`、`protobuf`等数据结构，编码成`byte[]`后，发送到客户端。

示例一：将数据封装为protobuf对象。

```
import jdk.internal.vm.annotation.ForceInline;

/**
 * Protobuf包体解析策略
 * <p>
 * 示例程序：将消息解析成{@link com.nutshell.nutshell.handler.PbPacket.Packet}对象。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 **/
public class ProtobufCoderPolicy implements ICoderStrategy<PbPacket.Packet> {

	@ForceInline
	@Override
	public PbPacket.Packet decoder(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
	}

	@Override
	public byte[] encoder(PbPacket.Packet packet) throws Exception {
		return packet.toByteArray();
	}
}
```

示例二：将数据封装为json对象。

```
import com.alibaba.fastjson.JSONObject;
import jdk.internal.vm.annotation.ForceInline;

/**
 * JSON包体解析策略
 * <p>
 * 示例程序：使用fastjson将消息解析成{@link com.alibaba.fastjson.JSONObject}对象。
 *
 * @author houyn[monkey@keimons.com]
 * @version 1.0
 * @since 11
 */
public class JsonParserPolicy implements IParserStrategy<JSONObject> {

	@ForceInline
	@Override
	public JSONObject decoder(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}

	@Override
	public byte[] encoder(JSONObject packet) throws Exception {
		return packet.toJSONString().getBytes();
	}
}
```

## 包体解析

当服务器收到消息后，需要将消息号和消息体从包体中解析出来，包体的数据结构可能是`json`、`protobuf`等数据结构。
同时，当消息处理完成后，需要将消息号、错误号、消息体等信息封装到包体中。

示例一：从protobuf中解析出消息号和消息体。

```
public class ProtobufPacketPolicy implements IPacketStrategy<PbPacket.Packet, ByteString> {

	@ForceInline
	@Override
	public int findMsgCode(PbPacket.Packet packet) {
		return packet.getMsgCode();
	}

	@ForceInline
	@Override
	public ByteString findData(PbPacket.Packet packet) {
		return packet.getData();
	}

	/**
	 * 构造一个消息体
	 *
	 * @param msgCode 消息号
	 * @param errCode 错误号
	 * @param message 消息体
	 * @return 消息体
	 */
	public PbPacket.Packet createPacket(int msgCode, String errCode, MessageLiteOrBuilder message) {
		PbPacket.Packet.Builder builder = PbPacket.Packet.newBuilder();
		builder.setMsgCode(msgCode);
		if (errCode != null) {
			builder.setErrCode(errCode);
		}
		if (message != null) {
			if (message instanceof MessageLite) {
				builder.setData(((MessageLite) message).toByteString());
			} else {
				builder.setData(((MessageLite.Builder) message).build().toByteString());
			}
		}
		return builder.build();
	}
}
```

示例二：从json中解析出消息号和消息体。

```
public class JsonPacketPolicy implements IPacketStrategy<JSONObject, JSONObject> {

	@ForceInline
	@Override
	public int findMsgCode(JSONObject packet) {
		return packet.getIntValue("msgCode");
	}

	@ForceInline
	@Override
	public JSONObject findData(JSONObject packet) {
		return packet.getJSONObject("data");
	}

	/**
	 * 构造一个消息体
	 *
	 * @param msgCode 消息号
	 * @param errCode 错误信息
	 * @param message 消息体
	 * @return 消息体
	 */
	public JSONObject createPacket(int msgCode, Object errCode, Object message) {
		JSONObject packet = new JSONObject();
		packet.put("msgCode", msgCode);
		if (errCode != null) {
			packet.put("errCode", msgCode);
		}
		if (message != null) {
			packet.put("data", message);
		}
		return packet;
	}
}
```