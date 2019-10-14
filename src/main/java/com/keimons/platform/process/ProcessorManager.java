package com.keimons.platform.process;

import com.keimons.platform.KeimonsConfig;
import com.keimons.platform.KeimonsServer;
import com.keimons.platform.annotation.AProcessor;
import com.keimons.platform.exception.ModuleException;
import com.keimons.platform.log.LogService;
import com.keimons.platform.network.Packet;
import com.keimons.platform.session.Session;
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
	private static Map<Integer, ProcessorInfo> processors = new HashMap<>();

	/**
	 * 选择适当的执行器
	 *
	 * @param session 会话
	 * @param packet  消息体
	 */
	public static void selectProcessor(Session session, Packet packet) {
		int msgCode = packet.getMsgCode();
		ProcessorInfo processorInfo = processors.get(msgCode);

		if (processorInfo != null) {
			switch (processorInfo.selectThreadLevel()) {
				case H_LEVEL:
					processorInfo.processor(session, packet);
					break;
				case M_LEVEL:
					ProcessorModel.asyncMidProcessor(session, processorInfo, packet);
					break;
				case L_LEVEL:
					ProcessorModel.asyncLowProcessor(session, processorInfo, packet);
					break;
				default:
			}
		} else {
			LogService.error("不存在的消息号：" + packet.getMsgCode());
		}
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
			AProcessor info = clazz.getAnnotation(AProcessor.class);
			if (info.ThreadLevel() == ThreadLevel.AUTO &&
					!KeimonsServer.KeimonsConfig.isAutoThreadLevel()) {
				throw new ModuleException("消息处理器不允许配置自适应线程池，因为未启用配置项：" + KeimonsConfig.DEFAULT_NET_THREAD_AUTO);
			}
			if ((info.ThreadLevel() == ThreadLevel.M_LEVEL &&
					KeimonsServer.KeimonsConfig.getNetThreadCount()[1] <= 0) ||
					KeimonsServer.KeimonsConfig.getNetThreadLevel()[1] < 0) {
				throw new ModuleException("消息处理器不允许配置中执行速度线程池，因为中执行速度线程池未开启");
			}
			if (info.ThreadLevel() == ThreadLevel.L_LEVEL &&
					KeimonsServer.KeimonsConfig.getNetThreadCount()[2] <= 0) {
				throw new ModuleException("消息处理器不允许配置低执行速度线程池，因为低执行速度线程池未开启");
			}
			if (processors.containsKey(info.MsgCode()) &&
					!clazz.getName().equals(processors.get(info.MsgCode()).getClass().getName())) {
				throw new ModuleException("重复的消息号：" + clazz.getName() + "，与：" + processors.get(info.MsgCode()).getClass().getName());
			}
			try {
				IProcessor processor = clazz.getDeclaredConstructor().newInstance();
				processors.put(info.MsgCode(), new ProcessorInfo(info, processor));
				System.out.println("消息处理器：" + "消息号：" + info.MsgCode() + "，描述：" + info.Desc());
			} catch (Exception e) {
				LogService.error(e, clazz.getSimpleName() + "安装消息处理器失败");
			}
			System.out.println("成功安装消息处理器：" + clazz.getSimpleName());
		}
	}
}