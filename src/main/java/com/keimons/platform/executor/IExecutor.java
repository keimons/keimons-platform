package com.keimons.platform.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 线程池
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IExecutor {

	/**
	 * 选择线程池执行业务
	 *
	 * @param type 线程池类型
	 * @param task 任务
	 */
	void execute(Enum<? extends IExecutorType> type, Runnable task);

	/**
	 * 选择线程池执行业务
	 *
	 * @param type  线程池类型
	 * @param index 线程池ID
	 * @param task  任务
	 */
	void execute(Enum<? extends IExecutorType> type, int index, Runnable task);

	/**
	 * 选择线程池执行业务并返回执行结果
	 *
	 * @param type      线程池类型
	 * @param task      任务
	 * @param <ResultT> 返回值类型
	 * @return 执行结果
	 * @throws ExecutionException   执行异常
	 * @throws InterruptedException 线程阻断异常
	 */
	<ResultT> ResultT execute(Enum<? extends IExecutorType> type, Callable<ResultT> task) throws ExecutionException, InterruptedException;

	/**
	 * 选择线程池执行业务并返回执行结果
	 *
	 * @param type      线程池类型
	 * @param index     线程池ID
	 * @param task      任务
	 * @param <ResultT> 返回值类型
	 * @return 执行结果
	 * @throws ExecutionException   执行异常
	 * @throws InterruptedException 线程阻断异常
	 */
	<ResultT> ResultT execute(Enum<? extends IExecutorType> type, int index, Callable<ResultT> task) throws ExecutionException, InterruptedException;
}