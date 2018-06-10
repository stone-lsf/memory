package com.stone.tc.momory.off.heap;

import com.stone.tc.memory.api.MemorySegmentPool;
import com.stone.tc.memory.api.MemorySegmentPoolFactory;
import com.stone.tc.memory.api.PoolContext;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:12
 */
public class OffHeapMemorySegmentPoolFactory extends MemorySegmentPoolFactory {
    @Override
    protected MemorySegmentPool doCreate(PoolContext poolContext) {
        return new OffHeapMemorySegmentPool(poolContext);
    }
}
