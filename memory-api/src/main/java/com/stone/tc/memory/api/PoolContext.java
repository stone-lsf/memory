package com.stone.tc.memory.api;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:04
 */
public class PoolContext {

    private final MemoryType memoryType;

    private final int maxSegments;

    private final int initialSegments;

    private final int segmentSize;

    public PoolContext(MemoryType memoryType, int maxSegments, int initialSegments, int segmentSize) {
        this.memoryType = memoryType;
        this.maxSegments = maxSegments;
        this.initialSegments = initialSegments;
        this.segmentSize = segmentSize;
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }

    public int getInitialSegments() {
        return initialSegments;
    }

    public int getSegmentSize() {
        return segmentSize;
    }

    public int getMaxSegments() {
        return maxSegments;
    }
}
