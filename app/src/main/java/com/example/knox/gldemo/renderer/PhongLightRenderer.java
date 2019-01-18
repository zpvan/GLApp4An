package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.MatrixUtil;
import com.example.knox.gldemo.utils.ResUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Knox.Tsang
 * @time 2019/1/18  14:27
 * @desc ${TODD}
 */

public class LightRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "LightRenderer";

    private static final int BYTES_OF_FLOAT = 4;
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String U_OBJECTCOLOR = "u_objectColor";
    private static final String U_LIGHTCOLOR = "u_lightColor";
    private static final int POSITION_COMPONENT_COUNT = 3;

    private Context mCtx;
    private int mSimpleProgram;
    private int mUMatrix;
    private int mAPosition;
    private int mUObjecColor;
    private int mULightColor;

    float boxVertices[] = {
            //  X      Y      Z
            //  z=-0.5的一面
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            //  z=0.5的一面
            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            //  x=-0.5的一面
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            //  x=0.5的一面
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            //  y=-0.5的一面
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
            //  y=0.5的一面
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
    };

    float boxTextures[] = {
            //  S     T
            //  z=-0.5的一面
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  0.0f,
            //  z=0.5的一面
            0.0f,  0.0f,
            1.0f,  0.0f,
            1.0f,  1.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  0.0f,
            //  x=-0.5的一面
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            //  x=0.5的一面
            1.0f,  0.0f,
            1.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  1.0f,
            0.0f,  0.0f,
            1.0f,  0.0f,
            //  y=-0.5的一面
            0.0f,  1.0f,
            1.0f,  1.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            0.0f,  0.0f,
            0.0f,  1.0f,
            //  y=0.5的一面
            0.0f,  1.0f,
            1.0f,  1.0f,
            1.0f,  0.0f,
            1.0f,  0.0f,
            0.0f,  0.0f,
            0.0f,  1.0f,
    };

    private long mBaseTimeMs;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mViewProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public LightRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0f, 0f, 0f, 1f);

        mSimpleProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.light_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.light_fragment_shader));

        if (mSimpleProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }

        GLES30.glUseProgram(mSimpleProgram);

        mUMatrix = GLES20.glGetUniformLocation(mSimpleProgram, U_MATRIX);
        mAPosition = GLES20.glGetAttribLocation(mSimpleProgram, A_POSITION);
        mUObjecColor = GLES20.glGetUniformLocation(mSimpleProgram, U_OBJECTCOLOR);
        mULightColor = GLES20.glGetUniformLocation(mSimpleProgram, U_LIGHTCOLOR);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES30.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        GLES30.glUseProgram(mSimpleProgram);

        MatrixUtil.perspectiveM(mProjectionMatrix, 45, (float) width
                / (float) height, 1f, 100f);

        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 1.7f, 5.1f,
                0, 0, 0,
                0, 1f, 0f);

        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        mBaseTimeMs = System.currentTimeMillis();
        GLES30.glUseProgram(0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES30.glUseProgram(mSimpleProgram);

        // 画立方体
        EsUtil.VertexAttribArrayAndEnable(mAPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, -2.0f);
        Matrix.rotateM(mModelMatrix, 0, 2f * ((System.currentTimeMillis() - mBaseTimeMs) / 100), 0.5f, 1f, 0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mUMatrix, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3f(mUObjecColor, 1.0f, 0.5f, 0.31f);
        GLES20.glUniform3f(mULightColor, 1.0f, 1.0f, 1.0f);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                boxVertices.length / POSITION_COMPONENT_COUNT);

        // 画光源
        EsUtil.VertexAttribArrayAndEnable(mAPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 10.0f, 0, -20.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mUMatrix, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3f(mUObjecColor, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(mULightColor, 1.0f, 1.0f, 1.0f);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                boxVertices.length / POSITION_COMPONENT_COUNT);

        GLES30.glUseProgram(0);
    }
}
