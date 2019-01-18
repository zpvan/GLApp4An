package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.AmbientRenderer;

public class AmbientActivity extends SingleGraphicActivity {

    private static final String TAG = "AmbientActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new AmbientRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
