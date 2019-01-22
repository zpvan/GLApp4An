package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.PointLightRenderer;

public class PointLightActivity extends SingleGraphicActivity {

    private static final String TAG = "PointLightActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new PointLightRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
