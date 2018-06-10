package com.stone.tc.memory.api;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午4:18
 */
public enum MemoryType {
    /**
     * indicate that is part of the java heap
     */
    HEAP,
    /**
     * indicate that is outside the java heap, but still part of the java process
     */
    OFF_HEAP;
}
