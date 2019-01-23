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
 * @time 2019/1/22  14:51
 * @desc ${TODD}
 */

public class MultiLightRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MultiLightRenderer";

    private static final String U_MATRIX = "u_Matrix";
    private static final String U_MODEL = "u_Model";
    private static final String U_VIEW = "u_View";
    private static final String U_PROJECTION = "u_Projection";
    private static final String A_POSITION = "a_Position";
    private static final String U_OBJECTCOLOR = "u_objectColor";
    private static final String U_LIGHTCOLOR = "u_lightColor";
    private static final String A_NORMALIZE = "a_Normalize";
    private static final String A_TEXTURECOORDS = "a_TextureCoords";
    private static final String U_NORMALIZEMODEL = "u_NormalizeModel";
    private static final String U_VIEWPOS = "u_ViewPos";
    private static final String U_MATERIAL_AMBIENT = "u_Material.ambient";
    private static final String U_MATERIAL_DIFFUSE = "u_Material.diffuse";
    private static final String U_MATERIAL_SPECULAR = "u_Material.specular";
    private static final String U_MATERIAL_SHININESS = "u_Material.shininess";

    // 多光源
    // 方向光
    private static final String U_DIRLIGHT_DIRECTION = "u_DirLight.direction";
    private static final String U_DIRLIGHT_AMBIENT = "u_DirLight.ambient";
    private static final String U_DIRLIGHT_DIFFUSE = "u_DirLight.diffuse";
    private static final String U_DIRLIGHT_SPECULAR = "u_DirLight.specular";

    // 4个点光源
    private final float[][] pointLightPositions = new float[][] {
        {0.7f,  0.2f,  2.0f},
        {2.3f, -3.3f, -4.0f},
        {-4.0f, 2.0f, -12.0f},
        {0.0f,  0.0f, -3.0f},
    };
    private static final String U_POINTLIGHTS_0_POSITION = "u_PointLights[0].position";
    private static final String U_POINTLIGHTS_0_CONSTANT = "u_PointLights[0].constant";
    private static final String U_POINTLIGHTS_0_LINEAR = "u_PointLights[0].linear";
    private static final String U_POINTLIGHTS_0_QUADRATIC = "u_PointLights[0].quadratic";
    private static final String U_POINTLIGHTS_0_AMBIENT = "u_PointLights[0].ambient";
    private static final String U_POINTLIGHTS_0_DIFFUSE = "u_PointLights[0].diffuse";
    private static final String U_POINTLIGHTS_0_SPECULAR = "u_PointLights[0].specular";

    private static final String U_POINTLIGHTS_1_POSITION = "u_PointLights[1].position";
    private static final String U_POINTLIGHTS_1_CONSTANT = "u_PointLights[1].constant";
    private static final String U_POINTLIGHTS_1_LINEAR = "u_PointLights[1].linear";
    private static final String U_POINTLIGHTS_1_QUADRATIC = "u_PointLights[1].quadratic";
    private static final String U_POINTLIGHTS_1_AMBIENT = "u_PointLights[1].ambient";
    private static final String U_POINTLIGHTS_1_DIFFUSE = "u_PointLights[1].diffuse";
    private static final String U_POINTLIGHTS_1_SPECULAR = "u_PointLights[1].specular";

    private static final String U_POINTLIGHTS_2_POSITION = "u_PointLights[2].position";
    private static final String U_POINTLIGHTS_2_CONSTANT = "u_PointLights[2].constant";
    private static final String U_POINTLIGHTS_2_LINEAR = "u_PointLights[2].linear";
    private static final String U_POINTLIGHTS_2_QUADRATIC = "u_PointLights[2].quadratic";
    private static final String U_POINTLIGHTS_2_AMBIENT = "u_PointLights[2].ambient";
    private static final String U_POINTLIGHTS_2_DIFFUSE = "u_PointLights[2].diffuse";
    private static final String U_POINTLIGHTS_2_SPECULAR = "u_PointLights[2].specular";

    private static final String U_POINTLIGHTS_3_POSITION = "u_PointLights[3].position";
    private static final String U_POINTLIGHTS_3_CONSTANT = "u_PointLights[3].constant";
    private static final String U_POINTLIGHTS_3_LINEAR = "u_PointLights[3].linear";
    private static final String U_POINTLIGHTS_3_QUADRATIC = "u_PointLights[3].quadratic";
    private static final String U_POINTLIGHTS_3_AMBIENT = "u_PointLights[3].ambient";
    private static final String U_POINTLIGHTS_3_DIFFUSE = "u_PointLights[3].diffuse";
    private static final String U_POINTLIGHTS_3_SPECULAR = "u_PointLights[3].specular";

    // 聚光灯
    private static final String U_SPOTLIGHT_DIRECTION = "u_SpotLight.direction";
    private static final String U_SPOTLIGHT_POSITION = "u_SpotLight.position";
    private static final String U_SPOTLIGHT_CUTOFF = "u_SpotLight.cutOff";
    private static final String U_SPOTLIGHT_OUTERCUTOFF = "u_SpotLight.outerCutOff";
    private static final String U_SPOTLIGHT_CONSTANT = "u_SpotLight.constant";
    private static final String U_SPOTLIGHT_LINEAR = "u_SpotLight.linear";
    private static final String U_SPOTLIGHT_QUADRATIC = "u_SpotLight.quadratic";
    private static final String U_SPOTLIGHT_AMBIENT = "u_SpotLight.ambient";
    private static final String U_SPOTLIGHT_DIFFUSE = "u_SpotLight.diffuse";
    private static final String U_SPOTLIGHT_SPECULAR = "u_SpotLight.specular";

    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMALIZE_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COMPONENT_COUNT = 2;

    private final float[] mEyePosition = {0.0f, 1.7f, 10.1f};
    private final float[] mEyeFront = {0.0f, 0.0f, -1.0f};

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
    private int uNormalizeModel;
    private int uViewPos;
    private int uMaterialAmbient;
    private int uMaterialDiffuse;
    private int uMaterialSpecular;
    private int uMaterialShininess;
    private int mWoodenDiffuseTex;
    private int mWoodenSpecularTex;
    private int mTextureCoords;

    // 方向光
    private int uDirLightDirection;
    private int uDirLightAmbient;
    private int uDirLightDiffuse;
    private int uDirLightSpecular;

    // 点光源
    private int uPointLights0Position;
    private int uPointLights0Constant;
    private int uPointLights0Linear;
    private int uPointLights0Quadratic;
    private int uPointLights0Ambient;
    private int uPointLights0Diffuse;
    private int uPointLights0Specular;

    private int uPointLights1Position;
    private int uPointLights1Constant;
    private int uPointLights1Linear;
    private int uPointLights1Quadratic;
    private int uPointLights1Ambient;
    private int uPointLights1Diffuse;
    private int uPointLights1Specular;

    private int uPointLights2Position;
    private int uPointLights2Constant;
    private int uPointLights2Linear;
    private int uPointLights2Quadratic;
    private int uPointLights2Ambient;
    private int uPointLights2Diffuse;
    private int uPointLights2Specular;

    private int uPointLights3Position;
    private int uPointLights3Constant;
    private int uPointLights3Linear;
    private int uPointLights3Quadratic;
    private int uPointLights3Ambient;
    private int uPointLights3Diffuse;
    private int uPointLights3Specular;

    // 聚光灯
    private int uSpotLightDirection;
    private int uSpotLightPosition;
    private int uSpotLightConstant;
    private int uSpotLightLinear;
    private int uSpotLightQuadratic;
    private int uSpotLightCutOff;
    private int uSpotLightOutCutOff;
    private int uSpotLightAmbient;
    private int uSpotLightDiffuse;
    private int uSpotLightSpecular;

    // 10个立方体的位置
    private final float[][] cubePositions = new float[][] {
            {0.0f,  0.0f,  0.0f},
            {2.0f,  5.0f, -15.0f},
            {-1.5f, -2.2f, -2.5f},
            {-3.8f, -2.0f, -12.3f},
            {2.4f, -0.4f, -3.5f},
            {-1.7f,  3.0f, -7.5f},
            {1.3f, -2.0f, -2.5f},
            {1.5f,  2.0f, -2.5f},
            {1.5f,  0.2f, -1.5f},
            {-1.3f,  1.0f, -1.5f},
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

    public MultiLightRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mObjectProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.multi_light_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.multi_light_fragment_shader));

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
        uNormalizeModel = GLES20.glGetAttribLocation(mObjectProgram, U_NORMALIZEMODEL);
        uViewPos = GLES20.glGetAttribLocation(mObjectProgram, U_VIEWPOS);
        uMaterialAmbient = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_AMBIENT);
        uMaterialDiffuse = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_DIFFUSE);
        uMaterialSpecular = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_SPECULAR);
        uMaterialShininess = GLES20.glGetUniformLocation(mObjectProgram, U_MATERIAL_SHININESS);
        mTextureCoords = GLES20.glGetAttribLocation(mObjectProgram, A_TEXTURECOORDS);

        // 方向光
        uDirLightDirection = GLES20.glGetUniformLocation(mObjectProgram, U_DIRLIGHT_DIRECTION);
        uDirLightAmbient = GLES20.glGetUniformLocation(mObjectProgram, U_DIRLIGHT_AMBIENT);
        uDirLightDiffuse = GLES20.glGetUniformLocation(mObjectProgram, U_DIRLIGHT_DIFFUSE);
        uDirLightSpecular = GLES20.glGetUniformLocation(mObjectProgram, U_DIRLIGHT_SPECULAR);

        // 点光源
        uPointLights0Position = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_POSITION);
        uPointLights0Constant = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_CONSTANT);
        uPointLights0Linear = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_LINEAR);
        uPointLights0Quadratic = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_QUADRATIC);
        uPointLights0Ambient = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_AMBIENT);
        uPointLights0Diffuse = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_DIFFUSE);
        uPointLights0Specular = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_0_SPECULAR);

        uPointLights1Position = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_POSITION);
        uPointLights1Constant = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_CONSTANT);
        uPointLights1Linear = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_LINEAR);
        uPointLights1Quadratic = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_QUADRATIC);
        uPointLights1Ambient = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_AMBIENT);
        uPointLights1Diffuse = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_DIFFUSE);
        uPointLights1Specular = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_1_SPECULAR);

        uPointLights2Position = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_POSITION);
        uPointLights2Constant = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_CONSTANT);
        uPointLights2Linear = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_LINEAR);
        uPointLights2Quadratic = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_QUADRATIC);
        uPointLights2Ambient = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_AMBIENT);
        uPointLights2Diffuse = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_DIFFUSE);
        uPointLights2Specular = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_2_SPECULAR);

        uPointLights3Position = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_POSITION);
        uPointLights3Constant = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_CONSTANT);
        uPointLights3Linear = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_LINEAR);
        uPointLights3Quadratic = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_QUADRATIC);
        uPointLights3Ambient = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_AMBIENT);
        uPointLights3Diffuse = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_DIFFUSE);
        uPointLights3Specular = GLES20.glGetUniformLocation(mObjectProgram, U_POINTLIGHTS_3_SPECULAR);

        // 聚光灯
        uSpotLightDirection = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_DIRECTION);
        uSpotLightPosition = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_POSITION);
        uSpotLightCutOff = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_CUTOFF);
        uSpotLightOutCutOff = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_OUTERCUTOFF);
        uSpotLightAmbient = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_AMBIENT);
        uSpotLightDiffuse = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_DIFFUSE);
        uSpotLightSpecular = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_SPECULAR);
        uSpotLightConstant = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_CONSTANT);
        uSpotLightLinear = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_LINEAR);
        uSpotLightQuadratic = GLES20.glGetUniformLocation(mObjectProgram, U_SPOTLIGHT_QUADRATIC);

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
            Matrix.translateM(modelMatrix, 0, cubePositions[i][0], cubePositions[i][1], cubePositions[i][2]);
            float angle = 20.0f * i;
            Matrix.rotateM(modelMatrix, 0, angle, 1.0f, 0.3f, 0.5f);
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
                mEyePosition[0] + mEyeFront[0], mEyePosition[1] + mEyeFront[1], mEyePosition[2] + mEyeFront[2],
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

        // 画立方体
        GLES20.glUseProgram(mObjectProgram);
        EsUtil.VertexAttribArrayAndEnable(aObjectPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);

        EsUtil.VertexAttribArrayAndEnable(aNormalize, NORMALIZE_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxNormalizedVertices);

        EsUtil.VertexAttribArrayAndEnable(mTextureCoords, TEXTURE_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxTextureVertices);

        GLES20.glUniform3f(uObjectObjecColor, 1.0f, 0.5f, 0.31f);
        GLES20.glUniform3f(uObjectLightColor, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uViewPos, mEyePosition[0], mEyePosition[1], mEyePosition[2]);

        GLES20.glUniform1i(uMaterialDiffuse, 0);
        GLES20.glUniform1i(uMaterialSpecular, 1);
        GLES20.glUniform1f(uMaterialShininess, 128 * 0.6f);

        // 方向光
        GLES20.glUniform3f(uDirLightDirection, -0.2f, -1.0f, -0.3f);
        GLES20.glUniform3f(uDirLightAmbient, 0.05f, 0.05f, 0.05f);
        GLES20.glUniform3f(uDirLightDiffuse, 0.4f, 0.4f, 0.4f);
        GLES20.glUniform3f(uDirLightSpecular, 0.5f, 0.5f, 0.5f);
        // 点光源1
        GLES20.glUniform3f(uPointLights0Position, pointLightPositions[0][0], pointLightPositions[0][1], pointLightPositions[0][2]);
        GLES20.glUniform3f(uPointLights0Ambient, 0.05f, 0.05f, 0.05f);
        GLES20.glUniform3f(uPointLights0Diffuse, 0.8f, 0.8f, 0.8f);
        GLES20.glUniform3f(uPointLights0Specular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform1f(uPointLights0Constant, 1.0f);
        GLES20.glUniform1f(uPointLights0Linear, 0.09f);
        GLES20.glUniform1f(uPointLights0Quadratic, 0.032f);
        // 点光源2
        GLES20.glUniform3f(uPointLights1Position, pointLightPositions[1][0], pointLightPositions[1][1], pointLightPositions[1][2]);
        GLES20.glUniform3f(uPointLights1Ambient, 0.05f, 0.05f, 0.05f);
        GLES20.glUniform3f(uPointLights1Diffuse, 0.8f, 0.8f, 0.8f);
        GLES20.glUniform3f(uPointLights1Specular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform1f(uPointLights1Constant, 1.0f);
        GLES20.glUniform1f(uPointLights1Linear, 0.09f);
        GLES20.glUniform1f(uPointLights1Quadratic, 0.032f);
        // 点光源3
        GLES20.glUniform3f(uPointLights2Position, pointLightPositions[2][0], pointLightPositions[2][1], pointLightPositions[2][2]);
        GLES20.glUniform3f(uPointLights2Ambient, 0.05f, 0.05f, 0.05f);
        GLES20.glUniform3f(uPointLights2Diffuse, 0.8f, 0.8f, 0.8f);
        GLES20.glUniform3f(uPointLights2Specular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform1f(uPointLights2Constant, 1.0f);
        GLES20.glUniform1f(uPointLights2Linear, 0.09f);
        GLES20.glUniform1f(uPointLights2Quadratic, 0.032f);
        // 点光源4
        GLES20.glUniform3f(uPointLights3Position, pointLightPositions[3][0], pointLightPositions[3][1], pointLightPositions[3][2]);
        GLES20.glUniform3f(uPointLights3Ambient, 0.05f, 0.05f, 0.05f);
        GLES20.glUniform3f(uPointLights3Diffuse, 0.8f, 0.8f, 0.8f);
        GLES20.glUniform3f(uPointLights3Specular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform1f(uPointLights3Constant, 1.0f);
        GLES20.glUniform1f(uPointLights3Linear, 0.09f);
        GLES20.glUniform1f(uPointLights3Quadratic, 0.032f);
        // 聚光灯
        GLES20.glUniform3f(uSpotLightPosition, mEyePosition[0], mEyePosition[1], mEyePosition[2]);
        GLES20.glUniform3f(uSpotLightDirection, mEyeFront[0], mEyeFront[1], mEyeFront[2]);
        GLES20.glUniform3f(uSpotLightAmbient, 0.0f, 0.0f, 0.0f);
        GLES20.glUniform3f(uSpotLightDiffuse, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform3f(uSpotLightSpecular, 1.0f, 1.0f, 1.0f);
        GLES20.glUniform1f(uSpotLightConstant, 1.0f);
        GLES20.glUniform1f(uSpotLightLinear, 0.09f);
        GLES20.glUniform1f(uSpotLightQuadratic, 0.032f);
        GLES20.glUniform1f(uSpotLightCutOff, (float) Math.cos(12.5f));
        GLES20.glUniform1f(uSpotLightOutCutOff, (float) Math.cos(15.0f));
        for (int i = 0; i < 10; i++) {
            GLES20.glUniformMatrix4fv(uObjectModel, 1, false, mModelMatrixs[i], 0);
            GLES20.glUniformMatrix4fv(uNormalizeModel, 1, false, mTransposeInvertModelMatrixs[i], 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,
                    boxVertices.length / POSITION_COMPONENT_COUNT);
        }
        GLES20.glUseProgram(0);

        // 画光源
        GLES20.glUseProgram(mLightProgram);
        EsUtil.VertexAttribArrayAndEnable(aLightPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, 0, boxVertices);

        for (int i = 0; i < 4; i++) {
            final float[] modelMatrix = new float[16];
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, pointLightPositions[i][0], pointLightPositions[i][1], pointLightPositions[i][2]);
            Matrix.scaleM(modelMatrix, 0, 0.1f, 0.1f, 0.1f);
            Matrix.multiplyMM(mMVPMatrix, 0, mViewProjectionMatrix, 0, modelMatrix, 0);
            GLES20.glUniformMatrix4fv(uLightMatrix, 1, false, mMVPMatrix, 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, boxVertices.length / POSITION_COMPONENT_COUNT);
        }
        GLES20.glUseProgram(0);
    }
}
