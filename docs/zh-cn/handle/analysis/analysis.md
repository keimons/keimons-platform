## 消息解析 <!-- {docsify-ignore-all} -->

由于网络层存在粘包/半包情况，所以，服务器在收到消息后，需要对数据流进行二次封装，构造出来一个完整的包体。 包体解析策略负责将`byte[]`解析成系统中可识别数据，例如`json`、`protobuf`等数据结构。

```
public interface IParserStrategy<PacketT> {

	/**
	 * 包体解析
	 * <p>
	 * 将消息解析成指定的消息类型，例如：json、protobuf等。
	 *
	 * @param packet 已经经过了二次封装的完整消息体
	 * @return 指定载体类型的包体
	 */
	@ForceInline
	PacketT parsePacket(byte[] packet) throws Exception;
}
```

本次操作对应数据流程：

```
+--------+       +--------+
| byte[] | ----> | packet |
+--------+       +--------+
```

在本设计中，包体中会包含消息号和消息体。

示例一：将数据封装为protobuf对象。

```
import jdk.internal.vm.annotation.ForceInline;

/**
 * Protobuf包体解析策略
 * <p>
 * 示例程序：将消息解析成{@link com.keimons.platform.handler.PbPacket.Packet}对象。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class ProtobufParserPolicy implements IParserStrategy<PbPacket.Packet> {

	@ForceInline
	@Override
	public PbPacket.Packet parsePacket(byte[] packet) throws Exception {
		return PbPacket.Packet.parseFrom(packet);
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
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public class JsonParserPolicy implements IParserStrategy<JSONObject> {

	@ForceInline
	@Override
	public JSONObject parsePacket(byte[] packet) throws Exception {
		return JSONObject.parseObject(new String(packet));
	}
}
```

## 包体解析

讲包体中的消息进行解析