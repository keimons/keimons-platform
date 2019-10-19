package com.keimons.platform.module;

import com.keimons.platform.KeimonsServer;
import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.iface.IPlayerData;
import com.keimons.platform.log.LogService;
import com.keimons.platform.unit.CharsetUtil;
import com.keimons.platform.unit.CodeUtil;
import org.redisson.client.codec.ByteArrayCodec;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 玩家数据加载器
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public class Loader implements Runnable {

	/**
	 * 正在加载中的队列
	 */
	private static LinkedBlockingDeque<Runnable> loading = new LinkedBlockingDeque<>();

	/**
	 * 立即加载玩家数据（阻塞加载）
	 *
	 * @param identifier 玩家唯一标识
	 * @return 玩家所有模块
	 */
	public static Modules fastLoad(String identifier) {
		AtomicReference<Modules> reference = new AtomicReference<>();
		FutureTask<Void> task = new FutureTask<>(getLoader(identifier, null, reference), null);
		loading.offerFirst(task);
		return reference.get();
	}

	/**
	 * 排队加载玩家数据
	 *
	 * @param identifier 玩家唯一标识
	 * @param consumer   消费函数
	 */
	public static void slowLoad(String identifier, Consumer<Modules> consumer) {
		loading.add(getLoader(identifier, consumer, null));
	}

	/**
	 * 排队加载
	 *
	 * @param runnable 执行过程
	 */
	public static void addLoad(Runnable runnable) {
		loading.add(runnable);
	}

	/**
	 * 阻塞加载
	 *
	 * @param callable 执行过程
	 * @param <R>      返回结果
	 * @return 返回值
	 * @throws ExecutionException   异常
	 * @throws InterruptedException 异常
	 */
	public static <R> R addLoad(Callable<R> callable) throws ExecutionException, InterruptedException {
		FutureTask<R> task = new FutureTask<>(callable);
		loading.offerFirst(task);
		return task.get();
	}

	/**
	 * 解码器
	 *
	 * @param identifier 玩家唯一表示
	 * @param consumer   加载成功后消费函数
	 * @param reference  引用
	 * @return 解码器
	 */
	public static Runnable getLoader(String identifier, Consumer<Modules> consumer, AtomicReference<Modules> reference) {
		return () -> {
			Modules modules = ModulesManager.getModules(identifier);
			if (modules == null) {
				modules = new Modules(identifier);
				int size = 0;
				Map<byte[], byte[]> bytes = RedissonManager.getMapValues(ByteArrayCodec.INSTANCE, identifier);
				for (Map.Entry<byte[], byte[]> entry : bytes.entrySet()) {
					size += entry.getKey().length;
					size += entry.getValue().length;
					String moduleName = CharsetUtil.getUTF8(entry.getKey());
					// 反序列化
					IPlayerData data = CodeUtil.decode(ModulesManager.classes.get(moduleName), entry.getValue());
					if (data != null) {
						modules.addPlayerData(data);
					}
				}
				if (KeimonsServer.KeimonsConfig.isDebug()) {
					LogService.debug("玩家ID：" + identifier + "，数据模块共计：" + size + "字节！");
				}
				modules.checkPlayerData();
				for (IPlayerData module : modules.getModules()) {
					module.decode();
				}
				for (IPlayerData data : modules.getModules()) {
					data.loaded(modules);
				}
			}
			ModulesManager.cacheModules(identifier, modules);
			if (consumer != null) {
				consumer.accept(modules);
			}
			if (reference != null) {
				reference.set(modules);
			}
		};
	}

	@Override
	public void run() {
		for (; ; ) {
			try {
				Runnable runnable = loading.take();
				runnable.run();
			} catch (Exception e) {
				LogService.error(e);
			}
		}
	}
}