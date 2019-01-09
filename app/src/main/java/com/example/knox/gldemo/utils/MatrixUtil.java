package com.example.knox.gldemo.utils;

import android.util.Log;

public class MatrixUtil {

    private static final String TAG = "MatrixUtil";

    public static String print(float[] matrix) {
        /**
         * for example
         * float[16] matrix in opengl
         *
         * line
         * | (0l,0c) (1l,0c) (2l,0c) (3l,0c) | column
         * | (0l,1c) (1l,1c) (2l,1c) (3l,1c) |
         * | (0l,2c) (1l,2c) (2l,2c) (3l,3c) |
         * | (0l,3c) (1l,3c) (2l,3c) (3l,3c) |
         *
         * so we need to print like this
         * | m[0] m[4] m[8]  m[12] |
         * | m[1] m[5] m[9]  m[13] |
         * | m[2] m[6] m[10] m[14] |
         * | m[3] m[7] m[11] m[15] |
         */
        StringBuilder body = new StringBuilder();
        body.append("\n");
        int length = matrix.length;
        double dColumn = Math.sqrt(length);
        try {
            Integer iColumn = Double.valueOf(dColumn).intValue();
            for (int i = 0; i < iColumn; i++) {
                body.append("| ");
                for (int j = 0; j < iColumn; j++) {
                    if (i + j * iColumn < matrix.length) {
                        body.append(matrix[i + j * iColumn]);
                        body.append(" ");
                    }
                }
                body.append("\n");
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "print: NumberFormatException=[" + dColumn + "]");
        }
        return body.toString();
    }

    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;
        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;
        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;
        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
