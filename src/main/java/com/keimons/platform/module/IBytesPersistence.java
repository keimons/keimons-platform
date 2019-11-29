package com.keimons.platform.module;

import com.keimons.platform.datebase.RedissonManager;
import com.keimons.platform.iface.IModule;
import org.redisson.client.codec.ByteArrayCodec;

/**
 * 二进制数据持久化
 * <p>
 * 将玩家数据序列化为二进制后的数据。
 *
 * @author monkey1993
 * @version 1.0
 * @since 1.8
 **/
public interface IBytesPersistence extends IPersistence, IModule {

    /**
     * 获取持久化数据
     *
     * @param notnull 是否强制获取数据
     * @return 最新数据{@code null}则表示无最新数据
     */
    byte[] persistence(boolean notnull);

    @Override
    default void save(String identifier, boolean coercive) {
        byte[] bytes = this.persistence(coercive);
        if (bytes != null) {
            RedissonManager.setMapValue(
                    ByteArrayCodec.INSTANCE, identifier, this.getModuleName(), bytes
            );
        }
    }
}