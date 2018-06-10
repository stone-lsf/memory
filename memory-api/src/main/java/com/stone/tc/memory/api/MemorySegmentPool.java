package com.stone.tc.memory.api;

import java.util.List;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午5:41
 */
public interface MemorySegmentPool {

    int getSegmentSize();

    int getAvailableSegments();

    MemoryType getMemoryType();

    MemorySegment allocate();

    List<MemorySegment> allocate(int size);

    void allocate(int size, List<MemorySegment> target);

    void recycle(MemorySegment segment);

    boolean isShutdown();

    void shutdown();

    void clear();
}
