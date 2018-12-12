package com.example.knox.gldemo.activity;

import android.opengl.GLSurfaceView;

import com.example.knox.gldemo.renderer.TexRectangleRenderer;

public class TexRectangleActivity extends SingleGraphicActivity {

    private static final String TAG = "TexRectangleActivity";

    @Override
    protected GLSurfaceView.Renderer getChildGlRenderer() {
        return new TexRectangleRenderer(this);
    }

    @Override
    protected String getChildActivityName() {
        return TAG;
    }
}
