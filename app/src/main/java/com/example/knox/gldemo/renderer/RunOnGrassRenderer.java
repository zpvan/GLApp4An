package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
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
 * @time 2019/1/8  9:18
 * @desc ${TODD}
 */

public class RunOnGrassRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "RunOnGrassRenderer";

    // vertex shader里边的uniform和attribute
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURECOORDINATES = "a_TextureCoordinates";
    // fragment shader里边的uniform
    private static final String U_TEXTUREUNIT = "u_TextureUnit";

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    private static final float TEXTURE_REPEAT = 40.0f;

    private Context mCtx;
    private int mGrassProgram;
    private int m_uMatrix;
    private int m_aPosition;
    private int m_aTextureCoordinates;
    private int m_uTextureUnit;

    private int mTextureObjectId;
    private final float[] mProjectionMatrix = new float[16];

    private float[] frontGrassVertices = new float[]{
            // X Y Z W
//            -1, 1, 1, 1,
//            1, 1, 1, 1,
//            -1, -1, 1, 1,
//
//            1, 1, 1, 1,
//            1, -1, 1, 1,
//            -1, -1, 1, 1,

            -8.0f, 8.0f, 0, 1,
            8.0f, 8.0f, 0, 1,
            -8.0f, -8.0f, 0, 1,

            8.0f, 8.0f, 0, 1,
            8.0f, -8.0f, 0, 1,
            -8.0f, -8.0f, 0, 1,
    };

    private float[] bottomGrassVertices = new float[]{
            // X Y Z W
            -1, -1, 1, 1,
            1, -1, 1, 1,
            -1, -1, -1, 1,

            1, -1, 1, 1,
            1, -1, -1, 1,
            -1, -1, -1, 1,
    };

    private float[] texGrassVertices = new float[]{
            // S T
            0 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,
            1 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,
            0 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,

            1 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,
            1 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,
            0 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,
    };


    public RunOnGrassRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mGrassProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.run_on_grass_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.run_on_grass_fragment_shader));

        if (mGrassProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }

        // 加载纹理图片
        mTextureObjectId = EsUtil.createSeamlessTexture2D(mCtx, R.drawable.caodi64);

        // 使用program的参数前, 要启动对应的program
        GLES20.glUseProgram(mGrassProgram);

        // 找到对应的gl变量
        m_uMatrix = GLES20.glGetUniformLocation(mGrassProgram, U_MATRIX);
        m_aPosition = GLES20.glGetAttribLocation(mGrassProgram, A_POSITION);
        m_aTextureCoordinates = GLES20.glGetAttribLocation(mGrassProgram, A_TEXTURECOORDINATES);
        m_uTextureUnit = GLES20.glGetUniformLocation(mGrassProgram, U_TEXTUREUNIT);

        // 使用program的参数后, 要关闭对应的program
        GLES20.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // 使用program的参数前, 要启动对应的program
        GLES20.glUseProgram(mGrassProgram);

        final float aspectRadio = (width > height) ? ((float) width / (float) height) : ((float) height / (float) width);
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRadio, aspectRadio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRadio, aspectRadio, -1f, 1f);
        }

        Log.e(TAG, "onSurfaceChanged: " + MatrixUtil.print(mProjectionMatrix));

        final float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -50f);

        final float[] projectionMatrix1 = new float[16];
        MatrixUtil.perspectiveM(projectionMatrix1, 45, (float) width
                / (float) height, 1f, 100f);
        Log.e(TAG, "onSurfaceChanged: [projectionMatrix1]=[" + MatrixUtil.print(projectionMatrix1) + "]");

        final float[] projectionMatrix2 = new float[16];
        Matrix.perspectiveM(projectionMatrix2,0, 45, (float) width
                / (float) height, 1f, 10f);
        Log.e(TAG, "onSurfaceChanged: [projectionMatrix2]=[" + MatrixUtil.print(projectionMatrix2) + "]");

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix1, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(m_uMatrix, 1, false, temp, 0);

        // 使用program的参数后, 要关闭对应的program
        GLES20.glUseProgram(0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 使用program的参数前, 要启动对应的program
        GLES20.glUseProgram(mGrassProgram);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureObjectId);
        GLES20.glUniform1i(m_uTextureUnit, 0);

        EsUtil.VertexAttribArrayAndEnable(m_aPosition, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, frontGrassVertices);

        EsUtil.VertexAttribArrayAndEnable(m_aTextureCoordinates, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, texGrassVertices);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
                frontGrassVertices.length / POSITION_COMPONENT_COUNT);

        // 使用program的参数后, 要关闭对应的program
        GLES20.glUseProgram(0);
    }
}
