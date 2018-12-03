package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.MatrixUtil;
import com.example.knox.gldemo.utils.NatBufUtil;
import com.example.knox.gldemo.utils.ResUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ScaleTriangleRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "ScaleTriangleRenderer";

    private static final String U_MATRIX   = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR    = "u_Color";

    private Context mCtx;

    private static final int     POSITION_COMPONENT_COUNT = 2;
    private final        float[] mProjectionMatrix        = new float[16];
    private final        float   mScaleX                  = 1.3f;
    private final        float   mScaleY                  = 1.6f;
    private final        float[] mIdentityMatrix          = new float[]{
            1f, 0, 0, 0,
            0, 1f, 0, 0,
            0, 0, 1f, 0,
            0, 0, 0, 1f,
    };
    private final        float[] mFinalMatrix             = new float[16];
    float[] triangleVertices = {
            0, 0,
            0, 0.7f,
            0.7f, 0,
    };
    private int mSimpleProgram;
    private int m_uMatrix;
    private int m_aPosition;
    private int m_uColor;

    private FloatBuffer vertexFloatBuffer;

    public ScaleTriangleRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        mSimpleProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.translate_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.simple_fragment_shader));

        if (mSimpleProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }
        GLES20.glUseProgram(mSimpleProgram);

        m_uMatrix = GLES20.glGetUniformLocation(mSimpleProgram, U_MATRIX);
        m_aPosition = GLES20.glGetAttribLocation(mSimpleProgram, A_POSITION);
        m_uColor = GLES20.glGetUniformLocation(mSimpleProgram, U_COLOR);

        vertexFloatBuffer = NatBufUtil.allocateFloatBuffer(triangleVertices);

        EsUtil.vertexAttribPointerAndEnable(m_aPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, vertexFloatBuffer);

        GLES20.glUniform4f(m_uColor, 1.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        final float aspectRadio = width > height ? ((float) width / (float) height) : ((float) height / (float) width);
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRadio, aspectRadio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRadio, aspectRadio, -1f, 1f);
        }
        Log.e(TAG, "onSurfaceChanged: [width, height]=[" + width + ", " + height + "]");
        Log.e(TAG, "onSurfaceChanged: mProjectionMatrix=[" + MatrixUtil.print(mProjectionMatrix) + "]");
        Matrix.scaleM(mIdentityMatrix, 0, mScaleX, mScaleY, 1f);
        Log.e(TAG, "onSurfaceChanged: mIdentityMatrix=[" + MatrixUtil.print(mIdentityMatrix) + "]");
        Matrix.multiplyMM(mFinalMatrix, 0, mIdentityMatrix, 0, mProjectionMatrix, 0);
        Log.e(TAG, "onSurfaceChanged: mFinalMatrix=[" + MatrixUtil.print(mFinalMatrix) + "]");
        GLES20.glUniformMatrix4fv(m_uMatrix, 1, false, mFinalMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
