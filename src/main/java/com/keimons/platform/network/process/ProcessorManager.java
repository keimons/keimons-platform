package com.keimons.platform.network.process;

import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.ClassUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理器管理
 */
public class ProcessorManager {

	/**
	 * 消息处理器
	 */
	private static Map<Integer, IProcessor> processor;

	/**
	 * 消息号信息
	 */
	private static Map<Integer, AProcessor> msgCodeInfo;

	public static IProcessor getProcessor(int msgCode) {
		return processor.get(msgCode);
	}

	public static AProcessor getMsgCodeInfo(int msgCode) {
		return msgCodeInfo.get(msgCode);
	}

	/**
	 * 初始化消息处理器
	 *
	 * @param packageName 包名
	 */
	public void init(String packageName) {
		Map<Integer, IProcessor> processor = new HashMap<>();
		Map<Integer, AProcessor> msgCodeInfo = new HashMap<>();
		System.out.print("消息处理器：");
		List<Class<IProcessor>> classes = ClassUtil.load(packageName, AProcessor.class, IProcessor.class);
		System.out.println("消息处理器：开始加载...");
		for (Class<IProcessor> clazz : classes) {
			AProcessor msgCode = clazz.getAnnotation(AProcessor.class);
			if (processor.containsKey(msgCode.MsgCode())) {
				System.err.println("重复的消息号：" + clazz.getName() + "，与：" + processor.get(msgCode.MsgCode()).getClass().getName());
				System.exit(0);
			}
			try {
				IProcessor iProcessor = clazz.newInstance();
				processor.put(msgCode.MsgCode(), iProcessor);
				msgCodeInfo.put(msgCode.MsgCode(), msgCode);
				System.out.println("消息处理器：" + "消息号：" + msgCode.MsgCode() + "，描述：" + msgCode.Desc());
			} catch (Exception e) {
				LogService.log(e);
			}
		}
		ProcessorManager.processor = processor;
		ProcessorManager.msgCodeInfo = msgCodeInfo;
		System.out.println("消息处理器：加载完成...");
		System.out.println();
	}

	public boolean shutdown() {
		return true;
	}
}