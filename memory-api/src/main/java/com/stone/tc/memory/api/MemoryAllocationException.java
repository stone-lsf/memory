package com.stone.tc.memory.api;

/**
 * @author shifeng.luo
 * @version created on 2018/6/11 下午9:21
 */
public class MemoryAllocationException extends Exception{

    private static final long serialVersionUID = -7893882233820788756L;

    public MemoryAllocationException() {
        super();
    }

    public MemoryAllocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemoryAllocationException(String message) {
        super(message);
    }

    public MemoryAllocationException(Throwable cause) {
        super(cause);
    }
}
