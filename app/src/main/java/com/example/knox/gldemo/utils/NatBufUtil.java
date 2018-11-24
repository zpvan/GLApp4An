package com.example.knox.gldemo.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class NatBufUtil {

    private static final int BYTES_PER_FLOAT = 4;

    public static FloatBuffer allocateFloatBuffer(float[] data) {
        FloatBuffer fb = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(data);

        fb.position(0);
        return fb;
    }
}
