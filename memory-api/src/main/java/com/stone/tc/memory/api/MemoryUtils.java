package com.stone.tc.memory.api;

import com.stone.tc.common.utils.MathUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午5:42
 */
@Slf4j
public class MemoryUtils {

    /**
     * The default memory page size. Currently set to 32 KiBytes.
     */
    private static final int DEFAULT_PAGE_SIZE = 32 * 1024;

    /**
     * The minimal memory page size. Currently set to 4 KiBytes.
     */
    private static final int MIN_PAGE_SIZE = 4 * 1024;


    public MemorySegmentPool newPool(long memorySize) {
        return newPool(memorySize, DEFAULT_PAGE_SIZE);
    }

    public MemorySegmentPool newPool(long memorySize, int pageSize) {
        return newPool(MemoryType.HEAP, memorySize, pageSize);
    }

    public MemorySegmentPool newPool(MemoryType memoryType, long memorySize, int pageSize) {
        return newPool(memoryType, memorySize, pageSize, true);
    }

    public MemorySegmentPool newPool(MemoryType memoryType, long memorySize, int pageSize, boolean preAllocated) {
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

        PoolContext poolContext = new PoolContext(memoryType, maxNumPages, toAllocate, pageSize);
        return MemorySegmentPoolFactory.create(poolContext);
    }
}
