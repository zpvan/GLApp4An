package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.CircleRenderer;

public class CircleActivity extends SingleGraphicActivity {

    private static final String TAG = "CircleActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new CircleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
