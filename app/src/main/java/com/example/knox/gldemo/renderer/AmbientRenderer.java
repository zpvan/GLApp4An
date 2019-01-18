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
 * @time 2019/1/18  15:13
 * @desc ${TODD}
 */

public class AmbientRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "AmbientRenderer";

    private static final int BYTES_OF_FLOAT = 4;
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String U_OBJECTCOLOR = "u_objectColor";
    private static final String U_LIGHTCOLOR = "u_lightColor";
    private static final int POSITION_COMPONENT_COUNT = 3;

    private Context mCtx;
    private int mObjectProgram;
    private int mLightProgram;
    private int uObjectMatrix;
    private int aObjectPosition;
    private int uObjectObjecColor;
    private int uObjectLightColor;
    private int uLightMatrix;
    private int aLightPosition;

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

    public AmbientRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0f, 0f, 0f, 1f);

        mObjectProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.ambient_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.ambient_fragment_shader));

        if (mObjectProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }

        GLES30.glUseProgram(mObjectProgram);

        uObjectMatrix = GLES20.glGetUniformLocation(mObjectProgram, U_MATRIX);
        aObjectPosition = GLES20.glGetAttribLocation(mObjectProgram, A_POSITION);
        uObjectObjecColor = GLES20.glGetUniformLocation(mObjectProgram, U_OBJECTCOLOR);
        uObjectLightColor = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHTCOLOR);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES30.glUseProgram(0);

        mLightProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.phonglight_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.light_fragment_shader));

        GLES30.glUseProgram(mLightProgram);

        uLightMatrix = GLES20.glGetUniformLocation(mLightProgram, U_MATRIX);
        aLightPosition = GLES20.glGetAttribLocation(mLightProgram, A_POSITION);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES30.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        MatrixUtil.perspectiveM(mProjectionMatrix, 45, (float) width
                / (float) height, 1f, 100f);

        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 1.7f, 5.1f,
                0, 0, 0,
                0, 1f, 0f);

        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        mBaseTimeMs = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES30.glUseProgram(mObjectProgram);
        // 画立方体
        EsUtil.VertexAttribArrayAndEnable(aObjectPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0, 0, -2.0f);
        Matrix.rotateM(mModelMatrix, 0, 2f * ((System.currentTimeMillis() - mBaseTimeMs) / 100), 0.5f, 1f, 0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(uObjectMatrix, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3f(uObjectObjecColor, 1.0f, 0.5f, 0.31f);
        GLES20.glUniform3f(uObjectLightColor, 1.0f, 1.0f, 1.0f);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                boxVertices.length / POSITION_COMPONENT_COUNT);
        GLES30.glUseProgram(0);

        GLES30.glUseProgram(mLightProgram);
        // 画光源
        EsUtil.VertexAttribArrayAndEnable(aLightPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 10.0f, 0, -20.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(uLightMatrix, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                boxVertices.length / POSITION_COMPONENT_COUNT);
        GLES30.glUseProgram(0);
    }
}
