package com.stone.tc.momory.off.heap;

import com.stone.tc.memory.api.MemorySegment;
import com.stone.tc.memory.api.MemorySegmentFactory;

import java.nio.ByteBuffer;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:23
 */
public class OffHeapMemorySegmentFactory implements MemorySegmentFactory {
    @Override
    public MemorySegment allocate(int segmentSize) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(segmentSize);
        return new OffHeapMemorySegment(byteBuffer);
    }
}
