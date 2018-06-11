package com.stone.tc.memory.api;

import com.stone.tc.common.ServiceLoader;
import com.stone.tc.common.utils.MathUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:12
 */
public class DefaultMemorySegmentPool implements MemorySegmentPool {
    /**
     * The default memory page size. Currently set to 32 KiBytes.
     */
    private static final int DEFAULT_PAGE_SIZE = 32 * 1024;

    /**
     * The minimal memory page size. Currently set to 4 KiBytes.
     */
    private static final int MIN_PAGE_SIZE = 4 * 1024;

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
    private int numNonAllocatedPages;

    /**
     * The collection of available memory segments.
     */
    private final ArrayDeque<MemorySegment> availableSegments;

    private boolean closed = false;

    private final MemorySegmentFactory segmentFactory;

    private final Object lock = new Object();

    private DefaultMemorySegmentPool(MemoryType memoryType, int segmentSize, int maxNumPages, int initialSegments, MemorySegmentFactory segmentFactory) {
        this.memoryType = memoryType;
        this.segmentSize = segmentSize;
        this.maxNumPages = maxNumPages;
        this.segmentFactory = segmentFactory;

        this.availableSegments = new ArrayDeque<>(initialSegments);

        for (int i = 0; i < initialSegments; i++) {
            MemorySegment segment = this.segmentFactory.allocate(segmentSize);
            availableSegments.add(segment);
        }

        this.numNonAllocatedPages = maxNumPages - initialSegments;
    }


    public static MemorySegmentPool newPool(long memorySize) {
        return newPool(memorySize, DEFAULT_PAGE_SIZE);
    }

    public static MemorySegmentPool newPool(long memorySize, int pageSize) {
        return newPool(MemoryType.HEAP, memorySize, pageSize);
    }

    public static MemorySegmentPool newPool(MemoryType memoryType, long memorySize, int pageSize) {
        return newPool(memoryType, memorySize, pageSize, true);
    }

    public static MemorySegmentPool newPool(MemoryType memoryType, long memorySize, int pageSize, boolean preAllocated) {
        if (memoryType == null) {
            throw new NullPointerException();
        }
        if (memorySize <= 0) {
            throw new IllegalArgumentException("Size of total memory must be positive.");
        }
        if (pageSize < MIN_PAGE_SIZE) {
            throw new IllegalArgumentException("The page size must be at least " + MIN_PAGE_SIZE + " bytes.");
        }
        if (!MathUtils.isPowerOf2(pageSize)) {
            throw new IllegalArgumentException("The given page size is not a power of two.");
        }

        if (memorySize < pageSize) {
            throw new IllegalArgumentException("The given amount of memory amounted to less than one page.");
        }


        final long numPagesLong = memorySize / pageSize;
        if (numPagesLong > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("The given number of memory bytes (" + memorySize
                    + ") corresponds to more than MAX_INT pages.");
        }
        int maxNumPages = (int) numPagesLong;
        int toAllocate = preAllocated ? maxNumPages : 0;

        String type = memoryType.name().toLowerCase();
        MemorySegmentFactory segmentFactory = ServiceLoader.findService(type, MemorySegmentFactory.class);
        return new DefaultMemorySegmentPool(memoryType, pageSize, maxNumPages, toAllocate, segmentFactory);
    }

    @Override
    public int getSegmentSize() {
        return segmentSize;
    }

    @Override
    public synchronized int getAvailableSegments() {
        synchronized (lock) {
            return numNonAllocatedPages + availableSegments.size();
        }
    }

    @Override
    public MemoryType getMemoryType() {
        return memoryType;
    }

    @Override
    public synchronized boolean isShutdown() {
        return closed;
    }

    @Override
    public synchronized void shutdown() {
        if (!closed) {
            closed = true;
            numNonAllocatedPages = 0;

            for (MemorySegment segment : availableSegments) {
                segment.free();
            }
            availableSegments.clear();
        }
    }

    @Override
    public synchronized MemorySegment allocate() throws MemoryAllocationException {
        if (numNonAllocatedPages < 1) {
            throw new MemoryAllocationException("Could not allocate one page. no pages are remaining.");
        }

        if (availableSegments.size() > 0) {
            return availableSegments.remove();
        } else {
            MemorySegment segment = segmentFactory.allocate(segmentSize);
            numNonAllocatedPages--;
            return segment;
        }
    }

    @Override
    public synchronized List<MemorySegment> allocate(int size) throws MemoryAllocationException {
        List<MemorySegment> result = new ArrayList<>(size);
        allocate(size, result);
        return result;
    }

    @Override
    public synchronized void allocate(int size, List<MemorySegment> target) throws MemoryAllocationException {
        if (numNonAllocatedPages < size) {
            throw new MemoryAllocationException("Could not allocate " + size + " page. only" + numNonAllocatedPages + " pages are remaining.");
        }

        while (availableSegments.size() > 0 && size > 0) {
            target.add(availableSegments.remove());
            size--;
        }

        while (size > 0) {
            MemorySegment segment = segmentFactory.allocate(segmentSize);
            numNonAllocatedPages--;
            target.add(segment);
            size--;
        }
    }

    @Override
    public synchronized void recycle(MemorySegment segment) {
        if (segment == null) {
            return;
        }

        if (segment.isFreed()) {
            return;
        }

        checkShutdown();
        segmentFactory.checkSegmentType(segment);

        availableSegments.add(segment);
        numNonAllocatedPages++;
    }

    private void checkShutdown() {
        if (closed) {
            throw new IllegalStateException("Memory pool has been shut down.");
        }
    }

    public int getMaxNumPages() {
        return maxNumPages;
    }
}
