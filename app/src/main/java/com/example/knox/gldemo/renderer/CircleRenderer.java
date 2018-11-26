package com.example.knox.gldemo.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.knox.gldemo.R;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.GraphicsUtil;
import com.example.knox.gldemo.utils.NatBufUtil;
import com.example.knox.gldemo.utils.ResUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CircleRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "CircleRenderer";

    private static final String A_POSITION               = "a_Position";
    private static final String U_COLOR                  = "u_Color";
    private static final String U_MATRIX                 = "u_Matrix";
    private static final int    POSITION_COMPONENT_COUNT = 2;
    private static final int    POINTS_OF_CIRCLE         = 100;

    private final float[] mProjectionMatrix = new float[16];

    private Context     mCtx;
    private int         mSimpleProgram;
    private int         m_uMatix;
    private int         m_aPosition;
    private int         m_uColor;
    private FloatBuffer vertexFloatBuffer;
    private float[]     circleVertices;

    public CircleRenderer(Context context) {
        mCtx = context;
    }

    private float[] generate2DCircleFloatArray(GraphicsUtil.Point center, float radius, int number) {
        float[] arrayOfCircumference = new float[(number + 1) * 2];
        double pieceOfangle = 2 * Math.PI / number;
        for (int i = 0; i <= number; i++) {
            arrayOfCircumference[i * 2] = (float) (center.x + radius * Math.cos(pieceOfangle * i));
            arrayOfCircumference[i * 2 + 1] = (float) (center.y + radius * Math.sin(pieceOfangle * i));
        }
        float[] arrayOfCenter = new float[]{center.x, center.y};
        float[] arrayOf2DCircle = new float[arrayOfCenter.length + arrayOfCircumference.length];
        System.arraycopy(arrayOfCenter, 0, arrayOf2DCircle, 0, arrayOfCenter.length);
        System.arraycopy(arrayOfCircumference, 0, arrayOf2DCircle, arrayOfCenter.length, arrayOfCircumference.length);
        return arrayOf2DCircle;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        mSimpleProgram = EsUtil.shaderCode2Program(
                ResUtil.readResource2String(mCtx, R.raw.circle_vertex_shader),
                ResUtil.readResource2String(mCtx, R.raw.simple_fragment_shader));

        if (mSimpleProgram == 0) {
            Log.e(TAG, "onSurfaceCreated: program failed!");
            return;
        }
        GLES20.glUseProgram(mSimpleProgram);

        m_uMatix = GLES20.glGetUniformLocation(mSimpleProgram, U_MATRIX);
        m_aPosition = GLES20.glGetAttribLocation(mSimpleProgram, A_POSITION);
        m_uColor = GLES20.glGetUniformLocation(mSimpleProgram, U_COLOR);

        circleVertices = generate2DCircleFloatArray(new GraphicsUtil.Point(0f, 0f), 0.7f, POINTS_OF_CIRCLE);

        vertexFloatBuffer = NatBufUtil.allocateFloatBuffer(circleVertices);

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
        GLES20.glUniformMatrix4fv(m_uMatix, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, circleVertices.length / POSITION_COMPONENT_COUNT);
    }
}
