package com.keimons.platform.process;

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
	private static Map<Integer, AProcessor> info;

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
		return info.get(msgCode);
	}

	/**
	 * 添加消息号
	 *
	 * @param packageName 消息处理器
	 */
	public static void addProcessor(String packageName) {
		List<Class<IProcessor>> classes = ClassUtil.load(packageName, AProcessor.class);
		for (Class<IProcessor> clazz : classes) {
			System.out.println("正在安装消息处理器：" + clazz.getSimpleName());
			AProcessor msgCode = clazz.getAnnotation(AProcessor.class);
			if (processors.containsKey(msgCode.MsgCode()) &&
					!clazz.getName().equals(processors.get(msgCode.MsgCode()).getClass().getName())) {
				LogService.error("重复的消息号：" + clazz.getName() + "，与：" + processors.get(msgCode.MsgCode()).getClass().getName());
			}
			try {
				IProcessor processor = clazz.newInstance();
				processors.put(msgCode.MsgCode(), processor);
				info.put(msgCode.MsgCode(), msgCode);
				System.out.println("消息处理器：" + "消息号：" + msgCode.MsgCode() + "，描述：" + msgCode.Desc());
			} catch (Exception e) {
				LogService.error(e, clazz.getSimpleName() + "安装消息处理器失败");
			}
			System.out.println("成功安装消息处理器：" + clazz.getSimpleName());
		}
	}
}