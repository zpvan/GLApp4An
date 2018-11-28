package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.TriangleRenderer;

public class TriangleActivity extends SingleGraphicActivity {

    private static final String TAG = "TriangleActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new TriangleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
