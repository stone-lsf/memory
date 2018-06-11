package com.stone.tc.memory.api;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午5:39
 */
public interface MemorySegmentFactory {

    MemorySegment allocate(int segmentSize);

    void checkSegmentType(MemorySegment segment);
}
