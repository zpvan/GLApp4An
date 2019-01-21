package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.LightTextureRenderer;

public class LightTextureActivity extends SingleGraphicActivity {

    private static final String TAG = "LightTextureActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new LightTextureRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
