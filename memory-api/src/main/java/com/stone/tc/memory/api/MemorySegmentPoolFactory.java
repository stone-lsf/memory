package com.stone.tc.memory.api;

import com.stone.tc.common.ServiceLoader;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:03
 */
public abstract class MemorySegmentPoolFactory {

    public static MemorySegmentPool create(PoolContext context) {
        String memoryType = context.getMemoryType().name().toLowerCase();
        MemorySegmentPoolFactory serializerFactory = ServiceLoader.findService(memoryType, MemorySegmentPoolFactory.class);

        return serializerFactory.doCreate(context);
    }

    protected abstract MemorySegmentPool doCreate(PoolContext types);
}
