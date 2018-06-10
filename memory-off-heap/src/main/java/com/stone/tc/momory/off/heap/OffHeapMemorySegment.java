package com.stone.tc.momory.off.heap;

import com.stone.tc.memory.api.MemorySegment;

import java.nio.ByteBuffer;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午6:21
 */
public class OffHeapMemorySegment implements MemorySegment {

    private final ByteBuffer offHeapBuffer;

    public OffHeapMemorySegment(ByteBuffer offHeapBuffer) {
        this.offHeapBuffer = offHeapBuffer;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public byte get(int offset) {
        return 0;
    }

    @Override
    public void put(int offset, byte b) {

    }

    @Override
    public void get(int offset, byte[] dst) {

    }

    @Override
    public void put(int offset, byte[] src) {

    }

    @Override
    public void get(int offset, byte[] dst, int start, int length) {

    }

    @Override
    public void put(int offset, byte[] src, int start, int length) {

    }

    @Override
    public boolean getBoolean(int offset) {
        return false;
    }

    @Override
    public void putBoolean(int offset, boolean value) {

    }

    @Override
    public char getChar(int offset) {
        return 0;
    }

    @Override
    public void putChar(int offset, char value) {

    }

    @Override
    public short getShort(int offset) {
        return 0;
    }

    @Override
    public void putShort(int offset, short value) {

    }

    @Override
    public int getInt(int offset) {
        return 0;
    }

    @Override
    public void putInt(int offset, int value) {

    }

    @Override
    public long getLong(int offset) {
        return 0;
    }

    @Override
    public void putLong(int offset, long value) {

    }

    @Override
    public float getFloat(int offset) {
        return 0;
    }

    @Override
    public void putFloat(int offset, float value) {

    }

    @Override
    public double getDouble(int offset) {
        return 0;
    }

    @Override
    public void putDouble(int offset, double value) {

    }

    @Override
    public void get(int offset, ByteBuffer target, int numBytes) {

    }

    @Override
    public void put(int offset, ByteBuffer source, int numBytes) {

    }
}
