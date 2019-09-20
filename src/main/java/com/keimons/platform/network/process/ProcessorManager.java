package com.keimons.platform.network.process;

import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理管理器
 *
 * @author monkey1993
 * @version 1.0
 * @date 2019-09-20
 * @since 1.8
 */
public class ProcessorManager {

	/**
	 * 消息处理器
	 */
	private static Map<Integer, IProcessor> processors = new HashMap<>();

	/**
	 * 消息描述
	 */
	private static Map<Integer, AProcessor> msgCodeInfo;

	/**
	 * 获取消息处理器
	 *
	 * @param msgCode 消息号
	 * @return 消息处理器
	 */
	public static IProcessor getProcessor(int msgCode) {
		return processors.get(msgCode);
	}

	/**
	 * 获取消息描述
	 *
	 * @param msgCode 消息号
	 * @return 消息描述
	 */
	public static AProcessor getMsgCodeInfo(int msgCode) {
		return msgCodeInfo.get(msgCode);
	}

	/**
	 * 初始化消息处理器
	 *
	 * @param packageName 包名
	 */
	public void init(String packageName) {
		Map<Integer, AProcessor> processorsInfo = new HashMap<>();
		System.out.print("消息处理器：");
		List<Class<IProcessor>> classes = ClassUtil.load(packageName, AProcessor.class, IProcessor.class);
		System.out.println("消息处理器：开始加载...");
		for (Class<IProcessor> clazz : classes) {
			AProcessor msgCode = clazz.getAnnotation(AProcessor.class);
			if (processors.containsKey(msgCode.MsgCode()) &&
					!clazz.getName().equals(processors.get(msgCode.MsgCode()).getClass().getName())) {
				LogService.error("重复的消息号：" + clazz.getName() + "，与：" + processors.get(msgCode.MsgCode()).getClass().getName());
				System.exit(0);
			}
			try {
				IProcessor processor = clazz.newInstance();
				processors.put(msgCode.MsgCode(), processor);
				processorsInfo.put(msgCode.MsgCode(), msgCode);
				System.out.println("消息处理器：" + "消息号：" + msgCode.MsgCode() + "，描述：" + msgCode.Desc());
			} catch (Exception e) {
				LogService.error(e);
			}
		}
		ProcessorManager.msgCodeInfo = processorsInfo;
		System.out.println("消息处理器：加载完成...");
		System.out.println();
	}

	public boolean shutdown() {
		return true;
	}
}