package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.glslReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "TriangleRenderer";

    private static final int BYTES_PER_FLOAT = 4;

    private static boolean LOG_ON = true;

    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";

    private Context mCtx;

    private static final int POSITION_COMPONENT_COUNT = 2;
    float[] tableVertices = {
            0, 0,
            0, 0.7f,
            0.9f, 0.7f,
    };
    private int mGlProgram;
    private int m_aPosition;
    private int m_uColor;

    int[] mCompileStatus;
    int[] mLinkStatus;
    int[] mValidateStatus;
    private FloatBuffer vertexFloatBuffer;

    public TriangleRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        int vertexShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // TODO 检查vertex shader是否创建成功
        if (vertexShaderId == 0) {
            if (LOG_ON) {
                Log.e(TAG, "could not create new vertex shader id");
            }
            return;
        }
        String verCode = glslReader.readResource2String(mCtx, R.raw.simple_vertex_shader);
        GLES20.glShaderSource(vertexShaderId, verCode);
        GLES20.glCompileShader(vertexShaderId);
        // TODO 检查vertex shader编译是否成功
        mCompileStatus = new int[1];
        GLES20.glGetShaderiv(vertexShaderId, GLES20.GL_COMPILE_STATUS, mCompileStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "Results of compiling source: \n" + verCode + "\n"
                + GLES20.glGetShaderInfoLog(vertexShaderId));
        }
        if (mCompileStatus[0] == 0) {
            // 编译shader失败
            GLES20.glDeleteShader(vertexShaderId);
            if (LOG_ON) {
                Log.e(TAG, "compile vertex shader failed");
            }
            return;
        }


        int fragmentShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        // TODO 检查fragment shader是否创建成功
        if (fragmentShaderId == 0) {
            if (LOG_ON) {
                Log.e(TAG, "could not create new fragment shader id");
            }
            return;
        }
        String fragCode = glslReader.readResource2String(mCtx, R.raw.simple_fragment_shader);
        GLES20.glShaderSource(fragmentShaderId, fragCode);
        GLES20.glCompileShader(fragmentShaderId);
        // TODO 检查fragment shader编译是否成功
        mCompileStatus = new int[1];
        GLES20.glGetShaderiv(fragmentShaderId, GLES20.GL_COMPILE_STATUS, mCompileStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "Results of compiling source: \n" + fragCode + "\n"
                + GLES20.glGetShaderInfoLog(fragmentShaderId));
        }
        if (mCompileStatus[0] == 0) {
            // 编译shader失败
            GLES20.glDeleteShader(vertexShaderId);
            if (LOG_ON) {
                Log.e(TAG, "compile fragment shader failed");
            }
            return;
        }

        mGlProgram = GLES20.glCreateProgram();
        // TODO 检查program是否创建成功
        if (mGlProgram == 0) {
            if (LOG_ON) {
                Log.e(TAG, "could not create gl program");
            }
            return;
        }

        GLES20.glAttachShader(mGlProgram, vertexShaderId);
        GLES20.glAttachShader(mGlProgram, fragmentShaderId);
        GLES20.glLinkProgram(mGlProgram);
        // TODO 检查program是否链接成功
        mLinkStatus = new int[1];
        GLES20.glGetProgramiv(mGlProgram, GLES20.GL_LINK_STATUS, mLinkStatus, 0);
        if (LOG_ON) {
            Log.e(TAG, "Results of linking program \n"
                    + GLES20.glGetProgramInfoLog(mGlProgram));
        }
        if (mLinkStatus[0] == 0) {
            GLES20.glDeleteProgram(mGlProgram);
            // 链接失败
            if (LOG_ON) {
                Log.e(TAG, "link gl program fail");
            }
            return;
        }
        // TODO 验证program是否有效
        if (LOG_ON) {
            GLES20.glValidateProgram(mGlProgram);

            mValidateStatus = new int[1];
            GLES20.glGetProgramiv(mGlProgram, GLES20.GL_VALIDATE_STATUS, mValidateStatus, 0);
            Log.e(TAG, "Results of Validating program \n" + mValidateStatus[0] + "\n Log: "
                + GLES20.glGetProgramInfoLog(mGlProgram));

            if (mValidateStatus[0] == 0) {
                Log.e(TAG, "validate gl program fail");
                return;
            }
        }

        GLES20.glUseProgram(mGlProgram);


        m_aPosition = GLES20.glGetAttribLocation(mGlProgram, A_POSITION);
        m_uColor = GLES20.glGetUniformLocation(mGlProgram, U_COLOR);

        vertexFloatBuffer = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(tableVertices);

        vertexFloatBuffer.position(0);

        // GLES20.glVertexAttrib4fv(m_aPosition, vertexFloatBuffer);
        GLES20.glVertexAttribPointer(m_aPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexFloatBuffer);
        GLES20.glEnableVertexAttribArray(m_aPosition);

        GLES20.glUniform4f(m_uColor, 1.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
