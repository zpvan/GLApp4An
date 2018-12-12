package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.NatBufUtil;
import com.example.knox.gldemo.utils.ResUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TexRectangleRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "TexRectangleRenderer";

    private static final String A_POSITION           = "a_Position";
    private static final String A_TEXTURECOORDINATES = "a_TextureCoordinates";
    private static final String U_MATRIX             = "u_Matrix";
    private static final String U_TEXTUREUNIT        = "u_TextureUnit";

    private final        float[] mProjectionMatrix        = new float[16];
    private static final int     POSITION_COMPONENT_COUNT = 2;
    private static final int     TEXTURE_COMPONENT_COUNT  = 2;
    private static final int     BYTES_OF_FLOAT           = 4;

    private Context     mCtx;
    private int         mSimpleProgram;
    private int         m_uMatix;
    private int         m_aPosition;
    private int         m_aTexturecoordinates;
    private int         m_uTextureunit;
    private FloatBuffer vertexFloatBuffer;
    private float[] rectangleVertices = new float[]{
            // x, y, s, t
            -0.8f, 0.8f, 0f, 0f,
            0.8f, 0.8f, 1f, 0f,
            0f, 0f, 0.5f, 0.5f,
            0.8f, -0.8f, 1f, 1f,
            -0.8f, -0.8f, 0f, 1f,
            -0.8f, 0.8f, 0f, 0f,
    };

    public TexRectangleRenderer(Context context) {
        mCtx = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);

        mSimpleProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.tex_rectangle_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.tex_rectangle_fragment_shader));

        if (mSimpleProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }
        GLES20.glUseProgram(mSimpleProgram);

        m_uMatix = GLES20.glGetUniformLocation(mSimpleProgram, U_MATRIX);
        m_uTextureunit = GLES20.glGetUniformLocation(mSimpleProgram, U_TEXTUREUNIT);
        m_aPosition = GLES20.glGetAttribLocation(mSimpleProgram, A_POSITION);
        m_aTexturecoordinates = GLES20.glGetAttribLocation(mSimpleProgram, A_TEXTURECOORDINATES);

        vertexFloatBuffer = NatBufUtil.allocateFloatBuffer(rectangleVertices);

        GLES20.glVertexAttribPointer(m_aPosition, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false,
                (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_OF_FLOAT,
                vertexFloatBuffer);

        GLES20.glEnableVertexAttribArray(m_aPosition);

        vertexFloatBuffer.position(POSITION_COMPONENT_COUNT);

        GLES20.glVertexAttribPointer(m_aTexturecoordinates, TEXTURE_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false,
                (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT) * BYTES_OF_FLOAT,
                vertexFloatBuffer);

        GLES20.glEnableVertexAttribArray(m_aTexturecoordinates);

        // generate a new texture id
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Log.e(TAG, "onSurfaceCreated: glGenTextures failed!");
            return;
        }

        // set wrap parameter
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        // set filter parameter
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // prepare bitmap data
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.tex128);
        if (bitmap == null) {
            Log.e(TAG, "onSurfaceCreated: decode bitmap failed!");
            return;
        }

        // load in texture
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // bind texture id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        // recycle bitmap
        bitmap.recycle();

        // unbind texture id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        // generate mipmap
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        // use number 0 texture unit
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindBuffer(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glUniform1i(m_uTextureunit, 0);
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0,
                rectangleVertices.length / (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT));
    }
}
