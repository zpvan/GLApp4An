package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.DirectLightRenderer;

public class DirectLightActivity extends SingleGraphicActivity {

    private static final String TAG = "DirectLightActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new DirectLightRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
