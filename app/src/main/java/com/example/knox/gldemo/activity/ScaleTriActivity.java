package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.ScaleTriangleRenderer;

public class ScaleTriActivity extends SingleGraphicActivity {

    private static final String TAG = "ScaleTriActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new ScaleTriangleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
