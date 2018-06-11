package com.stone.tc.memory.heap;

import com.stone.tc.memory.api.AbstractMemorySegment;

/**
 * @author shifeng.luo
 * @version created on 2018/6/11 下午8:16
 */
public class HeapMemorySegment extends AbstractMemorySegment {

    public HeapMemorySegment(byte[] buffer) {
        super(buffer);
    }
}
