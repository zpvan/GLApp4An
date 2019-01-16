package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knox.gldemo.renderer.ThreeDimensionalBoxRenderer;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.widget.NamedTextView;

public class ThreeDimensionalBoxActivity extends AppCompatActivity {

    private static final String TAG = "3DBoxActivity";

    private FrameLayout mRootFrameLayout;
    protected GLSurfaceView mGlSurfaceView;
    protected boolean rendererSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootFrameLayout = new FrameLayout(this);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new GLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(3);
            mGlSurfaceView.setRenderer(new ThreeDimensionalBoxRenderer(this));
            mRootFrameLayout.addView(mGlSurfaceView);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 3.0.", Toast.LENGTH_LONG).show();
        }

        NamedTextView namedTextView = new NamedTextView(this, TAG);
        mRootFrameLayout.addView(namedTextView);

        setContentView(mRootFrameLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet)
            mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet)
            mGlSurfaceView.onResume();
    }
}
