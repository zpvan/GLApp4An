package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.utils.WindowUtil;
import com.example.knox.gldemo.widget.NamedTextView;

abstract public class SingleGraphicActivity extends AppCompatActivity {

    private static final String TAG = "SingleGraphicActivity";

    protected GLSurfaceView mGlSurfaceView;
    protected boolean       rendererSet;

    private FrameLayout mRootFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowUtil.setLandscape(this);
        mRootFrameLayout = new FrameLayout(this);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new GLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(2);
            mGlSurfaceView.setRenderer(getChildGlRenderer());
            mRootFrameLayout.addView(mGlSurfaceView);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
        }

        NamedTextView namedTextView = new NamedTextView(this, getChildActivityName());
        mRootFrameLayout.addView(namedTextView);

        setContentView(mRootFrameLayout);
    }

    protected FrameLayout getRootFrameLayout() {
        return mRootFrameLayout;
    }

    protected abstract GLSurfaceView.Renderer getChildGlRenderer();

    protected abstract String getChildActivityName();

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) mGlSurfaceView.onResume();
    }
}
