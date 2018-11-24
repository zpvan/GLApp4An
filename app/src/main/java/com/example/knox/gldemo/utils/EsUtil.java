package com.example.knox.gldemo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;

public class EsUtil {

    private static final String TAG = "EsUtil";
    private static final boolean LOG_ON = true;

    private static final int GlEs2Version = 0x20000;

    /**
     * Only on Android, Judge the device support gles20 or not
     *
     * @param context
     * @return
     */
    public static boolean supportEs2(Context context) {

        boolean support = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager == null) {
            return support;
        }

        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        if (configurationInfo.reqGlEsVersion >= GlEs2Version || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")))) {
            support = true;
        }

        return support;
    }

    private static String shaderTypeInt2String(int type) {
        String sType = "";
        if (type == GLES20.GL_VERTEX_SHADER) {
            sType = "GLES20.GL_VERTEX_SHADER";
        }
        if (type == GLES20.GL_FRAGMENT_SHADER) {
            sType = "GLES20.GL_FRAGMENT_SHADER";
        }
        return sType;
    }

    private static int createShader(int type) {
        int shader = GLES20.glCreateShader(type);
        if (shader == 0) {
            if (LOG_ON) {
                Log.e(TAG, "createShader: failed! [type]=[" + shaderTypeInt2String(type) + "]");
            }
            return 0;
        }
        return shader;
    }

    private static int compileShader(int shader, String code) {
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "compileShader: Results of compiling source:\n" + code + "\n"
                    + GLES20.glGetShaderInfoLog(shader));
        }
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shader);
            if (LOG_ON) {
                Log.e(TAG, "compileShader: failed!");
            }
            return 0;
        }
        return shader;
    }

    private static int getVertexShader(String code) {
        int vertexShader = createShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShader == 0) {
            return 0;
        }
        vertexShader = compileShader(vertexShader, code);
        if (vertexShader == 0) {
            return 0;
        }
        return vertexShader;
    }

    private static int getFragmentShader(String code) {
        int fragmentShader = createShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShader == 0) {
            return 0;
        }
        fragmentShader = compileShader(fragmentShader, code);
        if (fragmentShader == 0) {
            return 0;
        }
        return fragmentShader;
    }

    private static int getProgram(int vertexShader, int fragmentShader) {
        int program = createProgram();
        if (program == 0) {
            return 0;
        }
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        program = linkProgram(program);
        if (program == 0) {
            return 0;
        }
        program = validateProgram(program);
        if (program == 0) {
            return 0;
        }
        return program;
    }

    private static int linkProgram(int program) {
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "linkProgram: Results of linking program \n"
                    + GLES20.glGetProgramInfoLog(program));
        }
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(program);
            if (LOG_ON) {
                Log.e(TAG, "linkProgram: failed!");
            }
            return 0;
        }
        return program;
    }

    private static int validateProgram(int program) {
        GLES20.glValidateProgram(program);
        int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "validateProgram: Results of Validating program \n" + validateStatus[0]
                    + "\n Log: " + GLES20.glGetProgramInfoLog(program));
        }
        if (validateStatus[0] == 0) {
            if (LOG_ON) {
                Log.e(TAG, "validateProgram: failed!");
            }
            return 0;
        }
        return program;
    }

    private static int createProgram() {
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            if (LOG_ON) {
                Log.e(TAG, "createProgram: failed!");
            }
            return 0;
        }
        return program;
    }

    public static int shaderCode2Program(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = getVertexShader(vertexShaderCode);
        if (vertexShader == 0) {
            return 0;
        }
        int fragmentShader = getFragmentShader(fragmentShaderCode);
        if (fragmentShader == 0) {
            return 0;
        }

        int program = getProgram(vertexShader, fragmentShader);
        return program;
    }

    public static void vertexAttribPointerAndEnable(int indx, int size, int type, boolean normalized,
                                                    int stride, java.nio.Buffer ptr) {
        GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
        GLES20.glEnableVertexAttribArray(indx);
    }


}
