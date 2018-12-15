package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.ResUtil;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MoveTexRectRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MoveTexRectRenderer";

    private static final String A_POSITION           = "a_Position";
    private static final String A_TEXTURECOORDINATES1 = "a_TextureCoordinates1";
    private static final String A_TEXTURECOORDINATES2 = "a_TextureCoordinates2";
    private static final String U_MATRIX             = "u_Matrix";
    private static final String U_TEXTUREUNIT1       = "u_TextureUnit1";
    private static final String U_TEXTUREUNIT2       = "u_TextureUnit2";

    private final        float[] mProjectionMatrix        = new float[16];
    private static final int     POSITION_COMPONENT_COUNT = 2;
    private static final int     TEXTURE_COMPONENT_COUNT  = 2;

    private Context     mCtx;
    private int         mSimpleProgram;
    private int         m_uMatix;
    private int         m_aPosition;
    private int         m_aTexturecoordinates1;
    private int         m_aTexturecoordinates2;
    private int         m_uTextureunit1;
    private int         m_uTextureunit2;
    private int         mTextureObjectId1;
    private int         mTextureObjectId2;
    private float[] rectangleVertices = new float[]{
            // x, y
            -0.8f, 0.8f,
            0.8f, 0.8f,
            0f, 0f,
            0.8f, -0.8f,
            -0.8f, -0.8f,
            -0.8f, 0.8f,
    };
    private float[] texture1Vertices = new float[] {
            // s, t
            0f, 0f,
            1f, 0f,
            0.5f, 0.5f,
            1f, 1f,
            0f, 1f,
            0f, 0f,
    };
    private float[] texture2Vertices = new float[] {
            0f, 0f,
            1f, 0f,
            0.5f, 0.5f,
            1f, 1f,
            0f, 1f,
            0f, 0f,
    };

    public MoveTexRectRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mSimpleProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.move_tex_rect_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.move_tex_rect_fragment_shader));

        if (mSimpleProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }
        GLES20.glUseProgram(mSimpleProgram);

        m_uMatix = GLES20.glGetUniformLocation(mSimpleProgram, U_MATRIX);
        m_uTextureunit1 = GLES20.glGetUniformLocation(mSimpleProgram, U_TEXTUREUNIT1);
        m_uTextureunit2 = GLES20.glGetUniformLocation(mSimpleProgram, U_TEXTUREUNIT2);
        m_aPosition = GLES20.glGetAttribLocation(mSimpleProgram, A_POSITION);
        m_aTexturecoordinates1 = GLES20.glGetAttribLocation(mSimpleProgram, A_TEXTURECOORDINATES1);
        m_aTexturecoordinates2 = GLES20.glGetAttribLocation(mSimpleProgram, A_TEXTURECOORDINATES2);

        EsUtil.VertexAttribArrayAndEnable(m_aPosition, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, rectangleVertices);

        EsUtil.VertexAttribArrayAndEnable(m_aTexturecoordinates1, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, texture1Vertices);

        EsUtil.VertexAttribArrayAndEnable(m_aTexturecoordinates2, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, texture2Vertices);

        mTextureObjectId1 = EsUtil.createTexture2D(mCtx, R.drawable.tex128);
        mTextureObjectId2 = EsUtil.createTexture2D(mCtx, R.drawable.tex256);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureObjectId1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureObjectId2);

        GLES20.glUniform1i(m_uTextureunit1, 0);
        GLES20.glUniform1i(m_uTextureunit2, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        final float aspectRadio = (width > height) ? ((float) width / (float) height) : ((float) height / (float) width);
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRadio, aspectRadio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRadio, aspectRadio, -1f, 1f);
        }

        GLES20.glUniformMatrix4fv(m_uMatix, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < texture2Vertices.length; i += TEXTURE_COMPONENT_COUNT) {
            texture2Vertices[i] += 0.005f;
        }
        EsUtil.VertexAttribArrayAndEnable(m_aTexturecoordinates2, TEXTURE_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, texture2Vertices);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
                rectangleVertices.length / POSITION_COMPONENT_COUNT);
    }
}
