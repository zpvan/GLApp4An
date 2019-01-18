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
import com.example.knox.gldemo.widget.ButtonCallback;
import com.example.knox.gldemo.widget.TouchEventCallback;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Knox.Tsang
 * @time 2019/1/8  9:18
 * @desc ${TODD}
 */

public class RunOnGrassRenderer implements GLSurfaceView.Renderer, TouchEventCallback, ButtonCallback {

    private static final String TAG = "RunOnGrassRenderer";

    // vertex shader里边的uniform和attribute
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_POSITION = "a_Position";
    private static final String A_TEXTURECOORDINATES = "a_TextureCoordinates";
    // fragment shader里边的uniform
    private static final String U_TEXTUREUNIT = "u_TextureUnit";

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;
    private static final float TEXTURE_REPEAT = 40.0f;
    private static final float GRASS_SIDE_LENGTH = 100.0f;
    private static final float YAW_SENSITIVITY = 0.005f;
    private static final float PITCH_SENSITIVITY = 0.002f;
    private static final float MOVE_SPEED = 0.3f;

    private Context mCtx;

    private int mGrassProgram;
    private int m_uMatrix;
    private int m_aPosition;
    private int m_aTextureCoordinates;
    private int m_uTextureUnit;

    private int mGrassTextureId;
    private int mBoxTextureId;

    private long mBaseTimeMs;
    private int mWidth;
    private int mHeight;
    private int mScope;
    private float mMove;
    private float mSpeed;
    private double mYaw;
    private double mPitch;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mGrassModelMatrix = new float[16];
    private final float[] mBoxModelMatrix = new float[16];

    private float[] bottomGrassVertices = new float[]{
            //   X                Y                    Z
            -GRASS_SIDE_LENGTH,  -1,   GRASS_SIDE_LENGTH,
             GRASS_SIDE_LENGTH,  -1,   GRASS_SIDE_LENGTH,
            -GRASS_SIDE_LENGTH,  -1,  -GRASS_SIDE_LENGTH,

             GRASS_SIDE_LENGTH,  -1,   GRASS_SIDE_LENGTH,
             GRASS_SIDE_LENGTH,  -1,  -GRASS_SIDE_LENGTH,
            -GRASS_SIDE_LENGTH,  -1,  -GRASS_SIDE_LENGTH,
    };

    private float[] texGrassVertices = new float[]{
            //               S                   T
            0 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,
            1 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,
            0 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,

            1 * TEXTURE_REPEAT, 1 * TEXTURE_REPEAT,
            1 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,
            0 * TEXTURE_REPEAT, 0 * TEXTURE_REPEAT,
    };

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
        mGrassTextureId = EsUtil.createSeamlessTexture2D(mCtx, R.drawable.caodi64);
        mBoxTextureId = EsUtil.createSeamlessTexture2D(mCtx, R.drawable.tex128);

        // 使用program的参数前, 要启动对应的program
        GLES20.glUseProgram(mGrassProgram);

        // 找到对应的gl变量
        m_uMatrix = GLES20.glGetUniformLocation(mGrassProgram, U_MATRIX);
        m_aPosition = GLES20.glGetAttribLocation(mGrassProgram, A_POSITION);
        m_aTextureCoordinates = GLES20.glGetAttribLocation(mGrassProgram, A_TEXTURECOORDINATES);
        m_uTextureUnit = GLES20.glGetUniformLocation(mGrassProgram, U_TEXTUREUNIT);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // 使用program的参数后, 要关闭对应的program
        GLES20.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        mBaseTimeMs = System.currentTimeMillis();
        mWidth = width;
        mHeight = height;
        mScope = 1;
        mMove = 0;
        mSpeed = 0.1f;
        mYaw = 0;
        mPitch = 0;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 使用program的参数前, 要启动对应的program
        GLES20.glUseProgram(mGrassProgram);

        // 设置好视角
        float fov = 45;
        fov = fov / mScope;
        if (fov <= 1.0)
            fov = 1.0f;
        if (fov >= 45.0)
            fov = 45.0f;
        MatrixUtil.perspectiveM(mProjectionMatrix, fov, (float) mWidth
                / (float) mHeight, 1f, 200f);

        // float radius = 10f;
        // double camX = Math.sin((System.currentTimeMillis() - mBaseTimeMs) / 5) * radius;
        // double camZ = Math.cos((System.currentTimeMillis() - mBaseTimeMs) / 5) * radius;

        float[] cameraPos = new float[] {0.0f + (mMove * (float) Math.sin(mYaw)), 0.0f, 20.0f - (mMove * (float) Math.cos(mYaw))};
        /**
         * 不能跑出草地
         */
        if (cameraPos[2] > GRASS_SIDE_LENGTH) {
            cameraPos[2] = GRASS_SIDE_LENGTH;
            mMove = (GRASS_SIDE_LENGTH - 20.0f) / (-(float) Math.cos(mYaw));
        }
        if (cameraPos[2] < -(GRASS_SIDE_LENGTH - 5)) {
            cameraPos[2] = -(GRASS_SIDE_LENGTH - 5);
            mMove = (-(GRASS_SIDE_LENGTH - 5) - 20.0f) / (-(float) Math.cos(mYaw));
        }

        float[] cameraFront = new float[] {(float) Math.sin(mYaw), (float) Math.sin(mPitch), -(((float) Math.cos(mYaw)) * ((float) Math.cos(mPitch)))};
        Matrix.setLookAtM(mViewMatrix, 0,
                cameraPos[0], cameraPos[1], cameraPos[2],
                cameraPos[0] + cameraFront[0], cameraPos[1] + cameraFront[1], cameraPos[2] + cameraFront[2],
                0, 1f, 0f);

        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        // 画草地
        EsUtil.VertexAttribArrayAndEnable(m_aPosition, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, bottomGrassVertices);

        EsUtil.VertexAttribArrayAndEnable(m_aTextureCoordinates, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, texGrassVertices);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mGrassTextureId);
        GLES20.glUniform1i(m_uTextureUnit, 0);

        Matrix.setIdentityM(mGrassModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mGrassModelMatrix, 0);
        GLES20.glUniformMatrix4fv(m_uMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                bottomGrassVertices.length / POSITION_COMPONENT_COUNT);

        // 画box
        EsUtil.VertexAttribArrayAndEnable(m_aPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);
        EsUtil.VertexAttribArrayAndEnable(m_aTextureCoordinates, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, boxTextures);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBoxTextureId);
        GLES20.glUniform1i(m_uTextureUnit, 0);

        Matrix.setIdentityM(mBoxModelMatrix, 0);
        Matrix.rotateM(mBoxModelMatrix, 0, 2f * ((System.currentTimeMillis() - mBaseTimeMs) / 100), 0.5f, 1f, 0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mBoxModelMatrix, 0);
        GLES20.glUniformMatrix4fv(m_uMatrix, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                boxVertices.length / POSITION_COMPONENT_COUNT);

        // 使用program的参数后, 要关闭对应的program
        GLES20.glUseProgram(0);
    }

    @Override
    public void moveCallback(float x, float y) {
        Log.e(TAG, "moveCallback: [x, y]=[" + x + ", " + y + "]");
        // 抬头低头
        mPitch += y * PITCH_SENSITIVITY;
        if (mPitch < 0) {
            mPitch = 0;
        }
        if (mPitch > 89) {
            mPitch = 89;
        }
        // 左右看
        mYaw += x * YAW_SENSITIVITY;
    }

    @Override
    public void scaleCallback(double scale) {
        Log.e(TAG, "scaleCallback: [scale]=[" + scale + "]");
    }

    @Override
    public void rotateCallback(float rotate) {
        Log.e(TAG, "rotateCallback: [rotate]=[" + rotate + "]");
    }

    @Override
    public void setXScope(int scope, boolean open) {
        Log.e(TAG, "setXScope: [scope]=[" + scope + "]");
        if (open) {
            mScope = scope;
        } else {
            mScope = 1;
        }
    }

    @Override
    public void move(boolean go) {
        if (go) {
            mMove += MOVE_SPEED;
        } else {
            mMove -= MOVE_SPEED;
        }
    }
}
