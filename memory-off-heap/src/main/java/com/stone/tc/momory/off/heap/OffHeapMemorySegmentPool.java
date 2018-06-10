package com.stone.tc.momory.off.heap;

import com.stone.tc.memory.api.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:12
 */
public class OffHeapMemorySegmentPool implements MemorySegmentPool {

    /**
     * The size of the memory segments.
     */
    private final int segmentSize;

    /**
     * The initial total size, for verification.
     */
    private final int maxNumPages;

    private final MemoryType memoryType;

    /**
     * The number of memory pages that have not been allocated and are available for lazy allocation.
     */
    private final AtomicInteger numNonAllocatedPages;

    /**
     * The collection of available memory segments.
     */
    private final ArrayDeque<MemorySegment> availableMemory;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final MemorySegmentFactory segmentFactory = new OffHeapMemorySegmentFactory();

    private final Object lock = new Object();

    public OffHeapMemorySegmentPool(PoolContext poolContext) {
        this.segmentSize = poolContext.getSegmentSize();
        this.maxNumPages = poolContext.getMaxSegments();
        this.memoryType = poolContext.getMemoryType();

        int initialSegments = poolContext.getInitialSegments();
        this.availableMemory = new ArrayDeque<>(initialSegments);

        for (int i = 0; i < initialSegments; i++) {
            MemorySegment segment = segmentFactory.allocate(segmentSize);
            availableMemory.add(segment);
        }

        this.numNonAllocatedPages = new AtomicInteger(maxNumPages - initialSegments);
    }

    @Override
    public int getSegmentSize() {
        return segmentSize;
    }

    @Override
    public int getAvailableSegments() {
        synchronized (lock) {
            return numNonAllocatedPages.get() + availableMemory.size();
        }
    }

    @Override
    public MemoryType getMemoryType() {
        return memoryType;
    }

    @Override
    public boolean isShutdown() {
        return closed.get();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public MemorySegment allocate() {
        return null;
    }

    @Override
    public List<MemorySegment> allocate(int size) {
        return null;
    }

    @Override
    public void allocate(int size, List<MemorySegment> target) {

    }

    @Override
    public void recycle(MemorySegment segment) {

    }

    @Override
    public void clear() {

    }
}
