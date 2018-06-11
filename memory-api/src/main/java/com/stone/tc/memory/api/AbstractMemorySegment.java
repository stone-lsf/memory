package com.stone.tc.memory.api;

import com.stone.tc.common.utils.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shifeng.luo
 * @version created on 2018/6/11 下午9:03
 */
public abstract class AbstractMemorySegment implements MemorySegment {
    private static final Unsafe UNSAFE = UnsafeUtil.UNSAFE;

    private final byte[] memory;

    /**
     * The beginning of the byte array contents, relative to the byte array object.
     */
    private static final long BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);

    private final long address;

    private final int size;

    private final long addressLimit;

    private final AtomicBoolean freed = new AtomicBoolean(false);

    public AbstractMemorySegment(byte[] buffer) {
        this.memory = buffer;
        this.size = buffer.length;
        this.address = BYTE_ARRAY_BASE_OFFSET;
        this.addressLimit = this.address + this.size;
    }

    /**
     * Creates a new memory segment that represents the memory at the absolute address given
     * by the pointer.
     *
     * @param offHeapAddress The address of the memory represented by this memory segment.
     * @param size           The size of this memory segment.
     */
    public AbstractMemorySegment(long offHeapAddress, int size) {
        if (offHeapAddress <= 0) {
            throw new IllegalArgumentException("negative pointer or size");
        }
        if (offHeapAddress >= Long.MAX_VALUE - Integer.MAX_VALUE) {
            // this is necessary to make sure the collapsed checks are safe against numeric overflows
            throw new IllegalArgumentException("Segment initialized with too large address: " + offHeapAddress
                    + " ; Max allowed address is " + (Long.MAX_VALUE - Integer.MAX_VALUE - 1));
        }

        this.memory = null;
        this.address = offHeapAddress;
        this.addressLimit = this.address + size;
        this.size = size;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public byte get(int offset) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos < addressLimit) {
            return UNSAFE.getByte(memory, pos);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void put(int offset, byte value) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos < addressLimit) {
            UNSAFE.putByte(memory, pos, value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void get(int offset, byte[] dst) {
        get(offset, dst, 0, dst.length);
    }

    @Override
    public void put(int offset, byte[] src) {
        put(offset, src, 0, src.length);
    }

    @Override
    public void get(int offset, byte[] dst, int start, int length) {
        checkFreed();
        if (offset < 0 || length < 0 || (start + length) < 0 || (dst.length - start - length) < 0) {
            throw new IndexOutOfBoundsException();
        }

        final long pos = address + offset;
        if (pos <= addressLimit - length) {
            final long arrayAddress = address + start;
            UNSAFE.copyMemory(memory, pos, dst, arrayAddress, length);
        }
    }

    @Override
    public void put(int offset, byte[] src, int start, int length) {
        checkFreed();
        if (offset < 0 || length < 0 || (start + length) < 0 || (src.length - start - length) < 0) {
            throw new IndexOutOfBoundsException();
        }

        final long pos = address + offset;
        if (pos <= addressLimit - length) {
            final long arrayAddress = address + start;
            UNSAFE.copyMemory(src, arrayAddress, memory, pos, length);
        }
    }

    @Override
    public boolean getBoolean(int offset) {
        return get(offset) != 0;
    }

    @Override
    public void putBoolean(int offset, boolean value) {
        put(offset, (byte) (value ? 1 : 0));
    }

    @Override
    public char getChar(int offset) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 2) {
            return UNSAFE.getChar(memory, pos);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void putChar(int offset, char value) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 2) {
            UNSAFE.putChar(memory, pos, value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public short getShort(int offset) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 2) {
            return UNSAFE.getShort(memory, pos);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void putShort(int offset, short value) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 2) {
            UNSAFE.putShort(memory, pos, value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getInt(int offset) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 4) {
            return UNSAFE.getInt(memory, pos);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void putInt(int offset, int value) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 4) {
            UNSAFE.putInt(memory, pos, value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public long getLong(int offset) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 8) {
            return UNSAFE.getLong(memory, pos);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void putLong(int offset, long value) {
        checkFreed();
        final long pos = address + offset;
        if (offset >= 0 && pos <= addressLimit - 8) {
            UNSAFE.putLong(memory, pos, value);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public float getFloat(int offset) {
        return Float.intBitsToFloat(getInt(offset));
    }

    @Override
    public void putFloat(int offset, float value) {
        putInt(offset, Float.floatToRawIntBits(value));
    }

    @Override
    public double getDouble(int offset) {
        return Double.longBitsToDouble(getLong(offset));
    }

    @Override
    public void putDouble(int offset, double value) {
        putLong(offset, Double.doubleToRawLongBits(value));
    }

    @Override
    public void get(int offset, ByteBuffer target, int numBytes) {
        checkFreed();
        if (offset < 0 || numBytes < 0) {
            throw new IndexOutOfBoundsException();
        }

        final int targetOffset = target.position();
        final int targetRemaining = target.remaining();
        if (targetRemaining < numBytes) {
            throw new BufferOverflowException();
        }

        synchronized (this) {
            if (target.isDirect()) {
                if (target.isReadOnly()) {
                    throw new ReadOnlyBufferException();
                }

                // copy to the target memory directly
                final long targetPointer = getAddress(target) + targetOffset;
                final long sourcePointer = address + offset;

                if (sourcePointer <= addressLimit - numBytes) {
                    UNSAFE.copyMemory(memory, sourcePointer, null, targetPointer, numBytes);
                    target.position(targetOffset + numBytes);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } else if (target.hasArray()) {
                // move directly into the byte array
                get(offset, target.array(), targetOffset + target.arrayOffset(), numBytes);

                // this must be after the get() call to ensue that the byte buffer is not
                // modified in case the call fails
                target.position(targetOffset + numBytes);
            } else {
                // neither heap buffer nor direct buffer
                while (target.hasRemaining()) {
                    target.put(get(offset++));
                }
            }
        }
    }

    @Override
    public void put(int offset, ByteBuffer source, int numBytes) {
        checkFreed();
        if (offset < 0 || numBytes < 0) {
            throw new IndexOutOfBoundsException();
        }

        final int sourceOffset = source.position();
        final int remaining = source.remaining();

        if (remaining < numBytes) {
            throw new BufferUnderflowException();
        }

        synchronized (this) {
            if (source.isDirect()) {
                // copy to the target memory directly
                final long sourcePointer = getAddress(source) + sourceOffset;
                final long targetPointer = address + offset;

                if (targetPointer <= addressLimit - numBytes) {
                    UNSAFE.copyMemory(null, sourcePointer, memory, targetPointer, numBytes);
                    source.position(sourceOffset + numBytes);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } else if (source.hasArray()) {
                // move directly into the byte array
                put(offset, source.array(), sourceOffset + source.arrayOffset(), numBytes);

                // this must be after the get() call to ensue that the byte buffer is not
                // modified in case the call fails
                source.position(sourceOffset + numBytes);
            } else {
                // neither heap buffer nor direct buffer
                while (source.hasRemaining()) {
                    put(offset++, source.get());
                }
            }
        }
    }

    @Override
    public void free() {
        freed.compareAndSet(false, true);
    }

    @Override
    public boolean isFreed() {
        return freed.get();
    }

    private void checkFreed() {
        if (freed.get()) {
            throw new IllegalStateException("segment had been freed");
        }
    }


    /**
     * The reflection fields with which we access the off-heap pointer from direct ByteBuffers.
     */
    private static final Field ADDRESS_FIELD;

    static {
        try {
            ADDRESS_FIELD = java.nio.Buffer.class.getDeclaredField("address");
            ADDRESS_FIELD.setAccessible(true);
        } catch (Throwable t) {
            throw new RuntimeException(
                    "Cannot initialize HybridMemorySegment: off-heap memory is incompatible with this JVM.", t);
        }
    }

    protected static long getAddress(ByteBuffer buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer is null");
        }
        try {
            return (Long) ADDRESS_FIELD.get(buffer);
        } catch (Throwable t) {
            throw new RuntimeException("Could not access direct byte buffer address.", t);
        }
    }
}
