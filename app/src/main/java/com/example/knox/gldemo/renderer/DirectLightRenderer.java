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
 * @time 2019/1/22  9:48
 * @desc ${TODD}
 */

public class DirectLightRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "DirectLightRenderer";

    private static final String U_MATRIX = "u_Matrix";
    private static final String U_MODEL = "u_Model";
    private static final String U_VIEW = "u_View";
    private static final String U_PROJECTION = "u_Projection";
    private static final String A_POSITION = "a_Position";
    private static final String U_OBJECTCOLOR = "u_objectColor";
    private static final String U_LIGHTCOLOR = "u_lightColor";
    private static final String A_NORMALIZE = "a_Normalize";
    private static final String U_LIGHTPOS = "u_lightPos";
    private static final String U_NORMALIZEMODEL = "u_NormalizeModel";
    private static final String U_VIEWPOS = "u_ViewPos";
    private static final String U_MATERIAL_AMBIENT = "u_Material.ambient";
    private static final String U_MATERIAL_DIFFUSE = "u_Material.diffuse";
    private static final String U_MATERIAL_SPECULAR = "u_Material.specular";
    private static final String U_MATERIAL_SHININESS = "u_Material.shininess";
    private static final String U_LIGHT_AMBIENT = "u_Light.ambient";
    private static final String U_LIGHT_DIFFUSE = "u_Light.diffuse";
    private static final String U_LIGHT_SPECULAR = "u_Light.specular";
    private static final String U_LIGHT_DIRECTION = "u_Light.direction";
    private static final String A_TEXTURECOORDS = "a_TextureCoords";
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMALIZE_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;

    private final float[] mEyePosition = {0.0f, 1.7f, 5.1f};
    private final float[] mLightPosition = {10.0f, 0.0f, -10.0f};

    private Context mCtx;
    private int mObjectProgram;
    private int mLightProgram;
    private int uObjectModel;
    private int uObjectView;
    private int uObjectProjection;
    private int aObjectPosition;
    private int aNormalize;
    private int uObjectObjecColor;
    private int uObjectLightColor;
    private int uLightMatrix;
    private int aLightPosition;
    private int uLightPos;
    private int uNormalizeModel;
    private int uViewPos;
    private int uMaterialAmbient;
    private int uMaterialDiffuse;
    private int uMaterialSpecular;
    private int uMaterialShininess;
    private int uLightAmbient;
    private int uLightDiffuse;
    private int uLightSpecular;
    private int uLightDirection;
    private int mWoodenDiffuseTex;
    private int mWoodenSpecularTex;
    private int mTextureCoords;

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

    float boxNormalizedVertices[] = {
            // Nx   Ny     Nz
            //  z=-0.5的一面
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            //  z=0.5的一面
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            //  x=-0.5的一面
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            //  x=0.5的一面
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            //  y=-0.5的一面
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            //  y=0.5的一面
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    float boxTextureVertices[] = {
            // S     T
            // z=-0.5的一面
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            // z=0.5的一面
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            // x=-0.5的一面
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            // x=0.5的一面
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            // y=-0.5的一面
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // y=0.5的一面
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
    };

    private long mBaseTimeMs;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mViewProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mInvertModelMatrix = new float[16];
    private float[] mTransposeInvertModelMatrix = new float[16];

    private float[][] mModelMatrixs = new float[10][16];
    private float[][] mInvertModelMatrixs = new float[10][16];
    private float[][] mTransposeInvertModelMatrixs = new float[10][16];

    public DirectLightRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mObjectProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.direct_light_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.direct_light_fragment_shader));

        if (mObjectProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }

        GLES20.glUseProgram(mObjectProgram);

        uObjectModel = GLES20.glGetUniformLocation(mObjectProgram, U_MODEL);
        uObjectView = GLES20.glGetUniformLocation(mObjectProgram, U_VIEW);
        uObjectProjection = GLES20.glGetUniformLocation(mObjectProgram, U_PROJECTION);
        aObjectPosition = GLES20.glGetAttribLocation(mObjectProgram, A_POSITION);
        uObjectObjecColor = GLES20.glGetUniformLocation(mObjectProgram, U_OBJECTCOLOR);
        uObjectLightColor = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHTCOLOR);
        aNormalize = GLES20.glGetAttribLocation(mObjectProgram, A_NORMALIZE);
        uLightPos = GLES20.glGetAttribLocation(mObjectProgram, U_LIGHTPOS);
        uNormalizeModel = GLES20.glGetAttribLocation(mObjectProgram, U_NORMALIZEMODEL);
        uViewPos = GLES20.glGetAttribLocation(mObjectProgram, U_VIEWPOS);
        uMaterialAmbient = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_AMBIENT);
        uMaterialDiffuse = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_DIFFUSE);
        uMaterialSpecular = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_SPECULAR);
        uMaterialShininess = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_SHININESS);
        // uLightAmbient = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHT_AMBIENT);
        uLightDiffuse = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHT_DIFFUSE);
        uLightSpecular = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHT_SPECULAR);
        uLightDirection = GLES20.glGetUniformLocation(mObjectProgram, U_LIGHT_DIRECTION);
        mTextureCoords = GLES20.glGetAttribLocation(mObjectProgram, A_TEXTURECOORDS);

        mWoodenDiffuseTex = EsUtil.createTexture2D(mCtx, R.drawable.wooden_box_diff_tex);
        mWoodenSpecularTex = EsUtil.createTexture2D(mCtx, R.drawable.wooden_box_spe_tex);
        /**
         * 所有创建纹理一定要在activeTexture之前, 要不总是会有纹理不对的问题
         */

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWoodenDiffuseTex);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mWoodenSpecularTex);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        for (int i = 0; i < 10; i++) {
            float[] modelMatrix = new float[16];
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, (float) (Math.random() * (i / 2 == 0 ? 1 : (-1)) * (20.0f)), 0, (float) (-30.0f * Math.random() - 2.0f));
            Matrix.rotateM(modelMatrix, 0, (float) (180.0f * Math.random()), 0.5f, 1f, 0f);
            // GLES20.glUniformMatrix4fv(uObjectModel, 1, false, mModelMatrix, 0);
            mModelMatrixs[i] = modelMatrix;

            float[] transposeInvertModelMatrix = new float[16];
            Matrix.invertM(mInvertModelMatrix, 0, modelMatrix, 0);
            Matrix.transposeM(transposeInvertModelMatrix, 0, mInvertModelMatrix, 0);
            // GLES20.glUniformMatrix4fv(uNormalizeModel, 1, false, mTransposeInvertModelMatrix, 0);
            mTransposeInvertModelMatrixs[i] = transposeInvertModelMatrix;
        }

        GLES20.glUseProgram(0);

        mLightProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.light_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.light_fragment_shader));

        GLES20.glUseProgram(mLightProgram);

        uLightMatrix = GLES20.glGetUniformLocation(mLightProgram, U_MATRIX);
        aLightPosition = GLES20.glGetAttribLocation(mLightProgram, A_POSITION);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glUseProgram(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        MatrixUtil.perspectiveM(mProjectionMatrix, 45, (float) width
                / (float) height, 1f, 100f);

        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.setLookAtM(mViewMatrix, 0, mEyePosition[0], mEyePosition[1], mEyePosition[2],
                0, 0, 0,
                0, 1f, 0f);

        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        mBaseTimeMs = System.currentTimeMillis();

        GLES20.glUseProgram(mObjectProgram);

        GLES20.glUniformMatrix4fv(uObjectProjection, 1, false, mProjectionMatrix, 0);
        GLES20.glUniformMatrix4fv(uObjectView, 1, false, mViewMatrix, 0);

        GLES20.glUseProgram(0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mObjectProgram);
        // 画立方体
        EsUtil.VertexAttribArrayAndEnable(aObjectPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);

        EsUtil.VertexAttribArrayAndEnable(aNormalize, NORMALIZE_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxNormalizedVertices);

        EsUtil.VertexAttribArrayAndEnable(mTextureCoords, TEXTURE_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxTextureVertices);

        GLES20.glUniform3f(uObjectObjecColor, 1.0f, 0.5f, 0.31f);
        GLES20.glUniform3f(uObjectLightColor, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uLightPos, mLightPosition[0], mLightPosition[1], mLightPosition[2]);
        GLES20.glUniform3f(uViewPos, mEyePosition[0], mEyePosition[1], mEyePosition[2]);

        GLES20.glUniform1i(uMaterialDiffuse, 0);
        GLES20.glUniform1i(uMaterialSpecular, 1);
        GLES20.glUniform1f(uMaterialShininess, 128 * 0.6f);

        GLES20.glUniform3f(uLightAmbient, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uLightDiffuse, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uLightSpecular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uLightDirection, -0.2f, -1.0f, -0.3f);

        for (int i = 0; i < 10; i++) {
            GLES20.glUniformMatrix4fv(uObjectModel, 1, false, mModelMatrixs[i], 0);
            GLES20.glUniformMatrix4fv(uNormalizeModel, 1, false, mTransposeInvertModelMatrixs[i], 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                    boxVertices.length / POSITION_COMPONENT_COUNT);
        }
        GLES20.glUseProgram(0);

        GLES20.glUseProgram(mLightProgram);
        // 画光源
        EsUtil.VertexAttribArrayAndEnable(aLightPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, mLightPosition[0], mLightPosition[1], mLightPosition[2]);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(uLightMatrix, 1, false, mMVPMatrix, 0);

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, boxVertices.length / POSITION_COMPONENT_COUNT);
        GLES20.glUseProgram(0);
    }
}
