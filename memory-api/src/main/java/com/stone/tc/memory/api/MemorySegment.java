package com.stone.tc.memory.api;

import java.nio.ByteBuffer;

/**
 * @author shifeng.luo
 * @version created on 2018/6/10 下午4:42
 */
public interface MemorySegment {

    int size();

    byte get(int offset);

    void put(int offset, byte b);

    void get(int offset, byte[] dst);

    void put(int offset, byte[] src);

    void get(int offset, byte[] dst, int start, int length);

    void put(int offset, byte[] src, int start, int length);

    boolean getBoolean(int offset);

    void putBoolean(int offset, boolean value);

    char getChar(int offset);

    void putChar(int offset, char value);

    short getShort(int offset);

    void putShort(int offset, short value);

    int getInt(int offset);

    void putInt(int offset, int value);

    long getLong(int offset);

    void putLong(int offset, long value);

    float getFloat(int offset);

    void putFloat(int offset, float value);

    double getDouble(int offset);

    void putDouble(int offset, double value);

    void get(int offset, ByteBuffer target, int numBytes);

    void put(int offset, ByteBuffer source, int numBytes);

    void free();

    boolean isFreed();
}
