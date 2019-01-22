package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.SpotLightRenderer;

public class SpotLightActivity extends SingleGraphicActivity {

    private static final String TAG = "SpotLightActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new SpotLightRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
