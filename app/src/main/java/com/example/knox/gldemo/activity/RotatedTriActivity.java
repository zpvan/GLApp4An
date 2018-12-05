package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.RotTriRenderer;

public class RotatedTriActivity extends SingleGraphicActivity {

    private static final String TAG = "RotatedTriActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new RotTriRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
