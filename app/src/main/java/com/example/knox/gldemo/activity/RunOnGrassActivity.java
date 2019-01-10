package com.example.knox.gldemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.knox.gldemo.renderer.RunOnGrassRenderer;
import com.example.knox.gldemo.utils.EsUtil;
import com.example.knox.gldemo.widget.NamedTextView;
import com.example.knox.gldemo.widget.TouchGLSurfaceView;

public class RunOnGrassActivity extends AppCompatActivity {

    private static final String TAG = "RunOnGrassActivity";

    protected TouchGLSurfaceView mGlSurfaceView;
    private RunOnGrassRenderer mRunOnGrassRenderer;
    protected boolean       rendererSet;

    private FrameLayout mRootFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootFrameLayout = new FrameLayout(this);

        if (EsUtil.supportEs2(this)) {
            mGlSurfaceView = new TouchGLSurfaceView(this);
            mGlSurfaceView.setEGLContextClientVersion(2);
            mRunOnGrassRenderer = new RunOnGrassRenderer(this);
            mGlSurfaceView.setRenderer(mRunOnGrassRenderer);
            mGlSurfaceView.setClickable(true);
            mGlSurfaceView.setTouchEventCallback(mRunOnGrassRenderer);
            mRootFrameLayout.addView(mGlSurfaceView);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
        }

        NamedTextView namedTextView = new NamedTextView(this, TAG);
        mRootFrameLayout.addView(namedTextView);

        setContentView(mRootFrameLayout);
    }

    protected FrameLayout getRootFrameLayout() {
        return mRootFrameLayout;
    }

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
