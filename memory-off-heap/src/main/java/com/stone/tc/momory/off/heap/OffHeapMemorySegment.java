package com.stone.tc.momory.off.heap;

import com.stone.tc.memory.api.AbstractMemorySegment;

import java.nio.ByteBuffer;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:21
 */
public class OffHeapMemorySegment extends AbstractMemorySegment {

    private final ByteBuffer offHeapBuffer;

    public OffHeapMemorySegment(ByteBuffer buffer) {
        super(checkBufferAndGetAddress(buffer), buffer.capacity());
        this.offHeapBuffer = buffer;
    }

    private static long checkBufferAndGetAddress(ByteBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        if (!buffer.isDirect()) {
            throw new IllegalArgumentException("Can't initialize from non-direct ByteBuffer.");
        }
        return getAddress(buffer);
    }

}
