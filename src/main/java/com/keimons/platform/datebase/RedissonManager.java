package com.keimons.platform.datebase;

import com.keimons.platform.iface.IManager;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据库管理
 */
public class RedissonManager implements IManager {

	/**
	 * 连接实例
	 */
	private static RedissonClient redisson;

	/**
	 * 获取redisson实例
	 *
	 * @return 连接实例
	 */
	public static RedissonClient getRedisson() {
		return redisson;
	}

	/**
	 * 获取一个键值对
	 *
	 * @param key 键
	 * @param <V> 返回值类型
	 * @return 值
	 */
	public static <V> V get(String key) {
		return get(StringCodec.INSTANCE, key);
	}

	/**
	 * 获取一个键值对
	 *
	 * @param codec 解码方式
	 * @param key   键
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <V> V get(Codec codec, String key) {
		RBucket<V> bucket = redisson.getBucket(key, codec);
		return bucket.get();
	}

	/**
	 * 获取数据
	 *
	 * @param keys 键
	 * @param <V>  返回值类型
	 * @return 值
	 */
	public static <V> Map<String, V> get(String... keys) {
		return get(StringCodec.INSTANCE, keys);
	}

	/**
	 * 获取数据
	 *
	 * @param codec 解码方式
	 * @param keys  键
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <V> Map<String, V> get(Codec codec, String... keys) {
		RBuckets buckets = redisson.getBuckets(codec);
		return buckets.get(keys);
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param key 键
	 * @param <F> 返回键类型
	 * @param <V> 返回值类型
	 * @return 所有field-value
	 */
	public static <F, V> Map<F, V> getMapValues(String key) {
		return getMapValues(StringCodec.INSTANCE, key);
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param codec 解码方式
	 * @param key   键
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 所有field-value
	 */
	public static <F, V> Map<F, V> getMapValues(Codec codec, String key) {
		RMap<F, V> map = redisson.getMap(key, codec);
		return map.readAllMap();
	}

	/**
	 * 获取Map中某一个字段的值
	 *
	 * @param key   键
	 * @param field 字段
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <F, V> V getMapValue(String key, F field) {
		return getMapValue(StringCodec.INSTANCE, key, field);
	}

	/**
	 * 获取Map中某一个字段的值
	 *
	 * @param codec 解码方式
	 * @param key   键
	 * @param field 字段
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <F, V> V getMapValue(Codec codec, String key, F field) {
		RMap<F, V> map = redisson.getMap(key, codec);
		return map.get(field);
	}

	/**
	 * 获取Multimap中某一个字段的所有值
	 *
	 * @param key 键
	 * @param <V> 返回值类型
	 * @return 值
	 */
	public static <V> Set<V> getSetMultimapAllValues(String key) {
		return getSetMultimapAllValues(StringCodec.INSTANCE, key);
	}

	/**
	 * 获取Multimap中某一个字段的所有值
	 *
	 * @param key   键
	 * @param field 字段
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <F, V> Set<V> getSetMultimapValues(String key, F field) {
		return getSetMultimapValues(StringCodec.INSTANCE, key, field);
	}

	/**
	 * 获取Multimap中某一个字段的所有值
	 *
	 * @param codec 解码器
	 * @param key   键
	 * @param field 字段
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <F, V> Set<V> getSetMultimapValues(Codec codec, String key, F field) {
		RSetMultimap<F, V> setMultimap = redisson.getSetMultimap(key, codec);
		return setMultimap.get(field).readAll();
	}

	/**
	 * 获取Multimap中某一个字段的所有值
	 *
	 * @param codec 解码器
	 * @param key   键
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <F, V> Set<V> getSetMultimapAllValues(Codec codec, String key) {
		RSetMultimap<F, V> setMultimap = redisson.getSetMultimap(key, codec);
		return setMultimap.entries().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
	}

	/**
	 * 获取指定Set下所有的值
	 *
	 * @param key 键
	 * @param <V> 返回值类型
	 * @return 值
	 */
	public static <V> Set<V> getSetValues(String key) {
		return getSetValues(StringCodec.INSTANCE, key);
	}

	/**
	 * 获取指定Set下所有的值
	 *
	 * @param codec 解码方式
	 * @param key   键
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <V> Set<V> getSetValues(Codec codec, String key) {
		RSet<V> set = redisson.getSet(key, codec);
		return set.readAll();
	}

	/**
	 * 获取指定计分排序集中元素
	 *
	 * @param codec      解码器
	 * @param key        键
	 * @param startScore 最低分
	 * @param endScore   最高分
	 * @param <V>        返回值类型
	 * @return 值
	 */
	public static <V> Collection<ScoredEntry<V>> getScoredSortedSetValues(Codec codec, String key, double startScore, double endScore) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key, codec);
		return set.entryRangeReversed(startScore, true, endScore, true);
	}

	/**
	 * 获取指定计分排序集中元素
	 *
	 * @param codec      编码解码器
	 * @param key        键
	 * @param startIndex 最低分
	 * @param endIndex   最高分
	 * @param <V>        返回值类型
	 * @return 值
	 */
	public static <V> Collection<ScoredEntry<V>> getScoredSortedSetValues(Codec codec, String key, int startIndex, int endIndex) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key, codec);
		return set.entryRange(startIndex, endIndex);
	}

	/**
	 * 获取指定计分排序集中元素
	 *
	 * @param codec 编码解码器
	 * @param key   键
	 * @param value 键
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <V> double getScoredSortedSetValue(Codec codec, String key, V value) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key, codec);
		Double score = set.getScore(value);
		if (score == null) {
			return 0;
		}
		return score;
	}

	/**
	 * 获取指定计分排序集中元素 倒序
	 *
	 * @param codec      编码解码器
	 * @param key        键
	 * @param startIndex 开始下标
	 * @param endIndex   结束下标
	 * @param <V>        返回值类型
	 * @return 值
	 */
	public static <V> Collection<ScoredEntry<V>> getScoredSortedSetValuesDesc(Codec codec, String key, int startIndex, int endIndex) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key, codec);
		return set.entryRangeReversed(startIndex, endIndex);
	}

	/**
	 * 删除 无尽模式 之前的排名信息
	 *
	 * @param codec 编码方式
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void delScoredSortedSetValuesDesc(Codec codec, String key, V value) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key, codec);
		set.remove(value);
	}

	/**
	 * 合并集合至新集合
	 *
	 * @param key1 新集合
	 * @param key2 源集合
	 */
	public static void getScoredSortedSetUnion(String key1, String key2) {
		RScoredSortedSet set = redisson.getScoredSortedSet(key1);
		set.union(key2);
	}

	/**
	 * 获取指定计分排序集中元素
	 *
	 * @param key 键
	 * @return 数量
	 */
	public static int getScoredSortedSetSize(String key) {
		RScoredSortedSet set = redisson.getScoredSortedSet(key);
		return set.size();
	}

	/**
	 * 获取自增主键
	 *
	 * @param key 键
	 * @return 值
	 */
	public static long incrementAndGet(String key) {
		RAtomicLong atomic = redisson.getAtomicLong(key);
		return atomic.incrementAndGet();
	}

	/**
	 * 获取自减主键
	 *
	 * @param key 键
	 * @return 值
	 */
	public static long decrementAndGet(String key) {
		RAtomicLong atomic = redisson.getAtomicLong(key);
		return atomic.decrementAndGet();
	}

	/**
	 * 设置一个键值对
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void set(String key, V value) {
		set(StringCodec.INSTANCE, key, value);
	}

	/**
	 * 设置一个键值对
	 *
	 * @param codec 编码解码器
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void set(Codec codec, String key, V value) {
		RBucket<V> bucket = redisson.getBucket(key, codec);
		bucket.set(value);
	}

	/**
	 * 设置一个键值对
	 *
	 * @param codec   编码解码器
	 * @param key     键
	 * @param value   值
	 * @param seconds 过期时间(秒)
	 * @param <V>     返回值类型
	 */
	public static <V> void set(Codec codec, String key, V value, int seconds) {
		RBucket<V> bucket = redisson.getBucket(key, codec);
		bucket.set(value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param codec 编码解码器
	 * @param key   键
	 * @param field 字段
	 * @param value 值
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 */
	public static <F, V> void setMapValue(Codec codec, String key, F field, V value) {
		RMap<F, V> map = redisson.getMap(key, codec);
		map.put(field, value);
	}


	/**
	 * Redis中的哈希桶
	 *
	 * @param key    键
	 * @param values 值
	 * @param <F>    返回键类型
	 * @param <V>    返回值类型
	 */
	public static <F, V> void setMapValues(String key, Map<F, V> values) {
		setMapValues(StringCodec.INSTANCE, key, values);
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param codec  编码解码器
	 * @param key    键
	 * @param values 值
	 * @param <F>    返回键类型
	 * @param <V>    返回值类型
	 */
	public static <F, V> void setMapValues(Codec codec, String key, Map<F, V> values) {
		RMap<F, V> map = redisson.getMap(key, codec);
		map.putAll(values);
	}

	/**
	 * 为指定Set下添加值
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void addSetValue(String key, V value) {
		RSet<V> set = redisson.getSet(key);
		set.add(value);
	}

	/**
	 * 新增指定计分排序集中元素
	 *
	 * @param key   键
	 * @param score 分数
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void addScoredSortedSetValue(String key, double score, V value) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key);
		set.add(score, value);
	}


	/**
	 * 新增指定计分排序集中元素，过期时间
	 *
	 * @param key        键
	 * @param score      分数
	 * @param value      值
	 * @param expireTime 过期时间
	 * @param <V>        返回值类型
	 */
	public static <V> void addScoredSortedSetValue(String key, double score, V value, long expireTime) {
		RScoredSortedSet<V> set = redisson.getScoredSortedSet(key);
		set.expire(expireTime, TimeUnit.MILLISECONDS);
		set.add(score, value);
	}

	/**
	 * 添加数据指定过期时间
	 *
	 * @param codec      编码解码器
	 * @param key        键
	 * @param value      值
	 * @param expireTime 过期时间
	 * @param <V>        返回值类型
	 */
	public static <V> void addSetValue(Codec codec, String key, V value, long expireTime) {
		RSet<V> set = redisson.getSet(key, codec);
		set.expire(expireTime, TimeUnit.MILLISECONDS);
		set.add(value);
	}

	/**
	 * set类型的设置过期时间
	 *
	 * @param key        键
	 * @param expireTime 过期时间
	 */
	public static void setKeyForExpireToSet(String key, long expireTime) {
		redisson.getSet(key).expire(expireTime, TimeUnit.MILLISECONDS);
	}

	/**
	 * 添加数据
	 *
	 * @param codec 编码方式
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void addSetValue(Codec codec, String key, V value) {
		RSet<V> set = redisson.getSet(key, codec);
		set.add(value);
	}

	/**
	 * 获取一个键值对
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void addListValue(String key, V value) {
		RQueue<V> queue = redisson.getQueue(key);
		queue.add(value);
	}

	/**
	 * 获取一个键值对
	 *
	 * @param codec 编码
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void addListValue(Codec codec, String key, V value) {
		RQueue<V> queue = redisson.getQueue(key, codec);
		queue.add(value);
	}

	/**
	 * 获取一个键值对
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 */
	public static <V> void each(String key, V value) {
		RQueue<V> queue = redisson.getQueue(key);
		queue.add(value);
	}

	/**
	 * 获取指定Set下所有的值
	 *
	 * @param codec 编码解码器
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 * @return 是否成功
	 */
	public static <V> boolean delSetValues(Codec codec, String key, V value) {
		RSet<V> set = redisson.getSet(key, codec);
		return set.remove(value);

	}

	/**
	 * 获取指定Set下的值
	 *
	 * @param key   键
	 * @param value 值
	 * @param <V>   返回值类型
	 * @return 是否成功
	 */
	public static <V> boolean delSetValues(String key, V value) {
		RSet<V> set = redisson.getSet(key);
		return set.remove(value);
	}

	/**
	 * 删除键
	 *
	 * @param keys 键
	 */
	public static void del(String... keys) {
		for (String key : keys) {
			RBucket<Object> bucket = redisson.getBucket(key);
			bucket.delete();
		}
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param codec 编码解码器
	 * @param key   键
	 * @param field 字段
	 */
	public static void delMapValue(Codec codec, String key, long field) {
		Iterator<Map.Entry<Object, Object>> iterator = redisson.getMap(key, codec).entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Object, Object> next = iterator.next();
			int key1 = (Integer) next.getKey();
			if ((int) field == key1) {
				iterator.remove();
			}
		}
	}

	/**
	 * Redis中的哈希桶
	 *
	 * @param key   键
	 * @param field 字段
	 * @param <F>   返回键类型
	 * @param <V>   返回值类型
	 */
	public static <F, V> void delMapValue(String key, F field) {
		RMap<F, V> map = redisson.getMap(key);
		map.remove(field);
	}

	/**
	 * 获取数据
	 *
	 * @param key 键
	 * @param <V> 返回值类型
	 * @return 值
	 */
	public static <V> List<V> getListValues(String key) {
		return getListValues(StringCodec.INSTANCE, key);
	}

	/**
	 * 获取数据
	 *
	 * @param codec 解码方式
	 * @param key   键
	 * @param <V>   返回值类型
	 * @return 值
	 */
	public static <V> List<V> getListValues(Codec codec, String key) {
		RList<V> list = redisson.getList(key, codec);
		return list.readAll();
	}

	/**
	 * 判断一个键是否存在
	 *
	 * @param key 键
	 * @return 是否存在
	 */
	public static boolean exists(String key) {
		return redisson.getBucket(key).isExists();
	}

	/**
	 * 移除列表中的元素
	 *
	 * @param key   键
	 * @param index 索引
	 */
	public static void removeListValue(String key, int index) {
		redisson.getList(key).remove(index);
	}

	public static <V> List<V> evalSha(String key, String sha, Object... params) {
		return redisson.getScript().evalSha(RScript.Mode.READ_WRITE,
				sha,
				RScript.ReturnType.MULTI,
				Collections.singletonList(key),
				params);
	}

	public static <V> List<V> eval(RedissonClient redisson, String key, String script, Object... params) {
		return redisson.getScript().eval(RScript.Mode.READ_WRITE,
				StringCodec.INSTANCE,
				script,
				RScript.ReturnType.MULTI,
				Collections.singletonList(key),
				params);
	}

	/**
	 * 初始化连接
	 *
	 * @param address    链接地址
	 * @param password   密码
	 * @param databaseId 数据库ID
	 * @return 数据库配置
	 */
	public Config init(String address, String password, int databaseId) {
		// 创建配置
		Config config = new Config();

		// 指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
		config.setCodec(StringCodec.INSTANCE);

		// 默认CPU * 2线程数
		// 考虑到项目并没有太多的IO操作，所以，修改为CPU / 2线程数
		config.setNettyThreads(Math.max(1, Runtime.getRuntime().availableProcessors() / 2));

		// 指定使用单节点部署方式
		config.useSingleServer().setAddress("redis://" + address);
		SingleServerConfig singleConfig = config.useSingleServer();

		// 设置密码
		singleConfig.setPassword(password);
		// 如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
		singleConfig.setIdleConnectionTimeout(10000);
		// 同任何节点建立连接时的等待超时。时间单位是毫秒。
		singleConfig.setConnectTimeout(30000);
		// 连接池最大容量。连接池的连接数量自动弹性伸缩
		singleConfig.setConnectionPoolSize(4);
		// 发布和订阅连接池
		singleConfig.setSubscriptionConnectionPoolSize(1);
		// 发布和订阅连接池
		singleConfig.setSubscriptionConnectionMinimumIdleSize(1);
		// 资源池确保最少空闲的连接数
		singleConfig.setConnectionMinimumIdleSize(2);
		// 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
		singleConfig.setTimeout(3000);
		singleConfig.setPingTimeout(30000);
		singleConfig.setDatabase(databaseId);
		return config;
	}

	public void init() {
		Config config1 = init("", "", 1); // redis共享分片

		// 创建客户端(发现这一非常耗时，基本在2秒-4秒左右)
		redisson = Redisson.create(config1);
	}
}