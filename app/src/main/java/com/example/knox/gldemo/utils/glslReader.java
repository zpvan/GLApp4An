package com.example.knox.gldemo.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class glslReader {
    /**
     *
     * @param context
     * @param resId  example: R.raw.simple_vertex_shader.glsl
     * @return
     */
    public static String readResource2String(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            /**
             * InputStream read => byte 或者 byte[]
             * InputStreamReader => char 或者 char[]
             * BufferedReader => char 或者 char[], 增强地可以readLine一整行char[]
             */
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }
}
