package com.keimons.platform.process;

/**
 * 系统中共计有逻辑处理的3个线程池，分为高中低三级，可以指定消息号
 * 采用某一个线程等级来运行，也可以采用动态{@code ThreadLevel#AUTO}来动态升级
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 */
public enum ThreadLevel {

	AUTO(-1), H_LEVEL(0), M_LEVEL(1), L_LEVEL(2);

	/**
	 * 运行线程优先级
	 */
	private int level;

	/**
	 * 构造方法
	 *
	 * @param level 线程等级
	 */
	ThreadLevel(int level) {
		this.level = level;
	}

	/**
	 * 获取线程的运行优先级
	 *
	 * @return 优先级
	 */
	public int getLevel() {
		return level;
	}
}