package com.stone.tc.memory.heap;

import com.stone.tc.memory.api.MemorySegment;
import com.stone.tc.memory.api.MemorySegmentFactory;

/**
 * @author shifeng.luo
 * @version created on 2018/6/11 下午8:17
 */
public class HeapMemorySegmentFactory implements MemorySegmentFactory {
    @Override
    public MemorySegment allocate(int segmentSize) {
        return new HeapMemorySegment(new byte[segmentSize]);
    }

    @Override
    public void checkSegmentType(MemorySegment segment) {
        if (!(segment instanceof HeapMemorySegment)) {
            throw new IllegalArgumentException("Memory type is not a " + HeapMemorySegment.class.getSimpleName());
        }
    }
}
