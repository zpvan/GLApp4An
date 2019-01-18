package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.LightRenderer;

public class PhoneLightActivity extends SingleGraphicActivity {

    private static final String TAG = "PhoneLightActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new LightRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
